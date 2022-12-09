import type { IClient } from "../lib/IClient.ts";
import type { IFetchPageParams } from "../lib/IFetchPageParams.ts";
import type { IPagination } from "../lib/IPagination.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import type { IURLResolver } from "../lib/IURLResolver.ts";
import type { IParser } from "../lib/IParser.ts";
import type { ILogger } from "../lib/ILogger.ts";
import { provide } from "provide";
import { PaginationResolver } from "../pagination-resolver/PaginationResolver.ts";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { OnPageParser } from "../on-page-parser/OnPageParser.ts";
import { ConsoleLogger } from "../logger/ConsoleLogger.ts";

export class PageListClient
  implements IClient<Array<IFetchPageParams>, [IPagination]>
{
  public constructor(
    private readonly pageResolver: IURLResolver<[pageNumber: number]>,
    private readonly httpClient: IHTMLClient,
    private readonly onPageParser: IParser<Array<IFetchPageParams>>,
    private readonly logger: ILogger
  ) {}

  private formatMessage(current: number, max: number, message: string): string {
    return `[${current}/${max}] ${message}`;
  }

  public async get(pagination: IPagination): Promise<Array<IFetchPageParams>> {
    const linksMap = new Map<string, IFetchPageParams>();
    for (let i = pagination.minPage; i <= pagination.maxPage; ++i) {
      this.logger.info(
        this.formatMessage(i, pagination.maxPage, `Start fetching...`)
      );
      const pageUrl = this.pageResolver.resolve(i);
      const content = await this.httpClient.get(pageUrl);
      const fetchPageParamsList = await this.onPageParser.parse(content);
      for (const fetchPageParams of fetchPageParamsList) {
        linksMap.set(fetchPageParams.url, fetchPageParams);
      }
      this.logger.info(
        this.formatMessage(
          i,
          pagination.maxPage,
          `Finished fetching. Stories found: ${fetchPageParamsList.length}`
        )
      );
    }
    return Array.from(linksMap.values());
  }
}

provide(PageListClient, [
  PaginationResolver,
  RetrierHTMLClient,
  OnPageParser,
  ConsoleLogger,
]);
