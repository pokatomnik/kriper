import type { IClient } from "../lib/IClient.ts";
import type { IFetchPageParams } from "../lib/IFetchPageParams.ts";
import type { IParser } from "../lib/IParser.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import type { IPageMeta } from "../../domain/IPageMeta.ts";
import type { ILogger } from "../lib/ILogger.ts";
import { provide } from "provide";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { PageMetaParser } from "../pagemeta-parser/PageMetaParser.ts";
import { ConsoleLogger } from "../logger/ConsoleLogger.ts";

export class PageMetaClient
  implements
    IClient<ReadonlyArray<IPageMeta>, [ReadonlyArray<IFetchPageParams>]>
{
  public constructor(
    private readonly httpClient: IHTMLClient,
    private readonly pageMetaParser: IParser<IPageMeta>,
    private readonly logger: ILogger
  ) {}

  private formatMessage(postition: number, total: number, message: string) {
    return `[${postition}/${total}] ${message}`;
  }

  public async get(
    fetchParamsList: ReadonlyArray<IFetchPageParams>
  ): Promise<ReadonlyArray<IPageMeta>> {
    const pageMetaList: Array<IPageMeta> = [];
    const totalPages = fetchParamsList.length;
    for (let i = 0; i < fetchParamsList.length; ++i) {
      const fetchParams = fetchParamsList[i];
      if (!fetchParams) continue;

      try {
        this.logger.info(
          this.formatMessage(
            i,
            totalPages,
            `Started working on ${fetchParams.title}. Related URL: ${fetchParams.url}`
          )
        );
        const rawHTML = await this.httpClient.get(fetchParams.url);
        const pageMeta = await this.pageMetaParser.parse(rawHTML);
        pageMetaList.push(pageMeta);
        this.logger.info(
          this.formatMessage(i, totalPages, "Successfully parsed.")
        );
      } catch (e) {
        const error =
          e instanceof Error ? e : new Error("Unknown PageMeta parser error");
        this.logger.error(
          this.formatMessage(
            i,
            totalPages,
            `Failed to parse PageMeta. Original error message: ${error.message}.`
          )
        );
      }
    }

    return pageMetaList;
  }
}

provide(PageMetaClient, [RetrierHTMLClient, PageMetaParser, ConsoleLogger]);
