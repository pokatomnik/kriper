import type { IClient } from "services/lib/IClient.ts";
import type { IFetchPageParams } from "services/lib/IFetchPageParams.ts";
import type { IParser } from "services/lib/IParser.ts";
import type { IHTMLClient } from "services/network/IHTMLClient.ts";
import type { IPageMeta } from "domain/IPageMeta.ts";
import type { ILogger } from "services/lib/ILogger.ts";
import { Provide } from "microdi";
import { RetrierHTMLClient } from "services/network/RetrierHTMLClient.ts";
import { PageMetaParser } from "services/pagemeta-parser/PageMetaParser.ts";
import { ConsoleLogger } from "services/logger/ConsoleLogger.ts";
import { BoundMethod } from "decorate";
import { ShortDescriptionClient } from "services/short-description-client/ShortDescriptionClient.ts";
import { ShortDescriptionSaver } from "services/storage/ShortDescriptionSaver.ts";
import { IAsyncStorage } from "services/lib/IAsyncStorage.ts";

@Provide(
  RetrierHTMLClient,
  ShortDescriptionClient,
  ShortDescriptionSaver,
  PageMetaParser,
  ConsoleLogger
)
export class PageMetaClient
  implements
    IClient<ReadonlyArray<IPageMeta>, [ReadonlyArray<IFetchPageParams>]>
{
  public constructor(
    private readonly httpClient: IHTMLClient,
    private readonly shortDescriptionClient: IClient<string | null, [string]>,
    private readonly shortDescriptionSaver: IAsyncStorage<string, string>,
    private readonly pageMetaParser: IParser<IPageMeta>,
    private readonly logger: ILogger
  ) {}

  private formatMessage(postition: number, total: number, message: string) {
    return `[${postition}/${total}] ${message}`;
  }

  @BoundMethod
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
        const shortDescritption = await this.shortDescriptionClient.get(
          fetchParams.url
        );
        if (shortDescritption) {
          await this.shortDescriptionSaver.set(
            pageMeta.storyId,
            shortDescritption
          );
        }
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
