import type { IClient } from "services/lib/IClient.ts";
import type { IFetchPageParams } from "services/lib/IFetchPageParams.ts";
import type { IPagination } from "services/lib/IPagination.ts";
import type { IHTMLClient } from "services/network/IHTMLClient.ts";
import type { IURLResolver } from "services/lib/IURLResolver.ts";
import type { IParser } from "services/lib/IParser.ts";
import type { ILogger } from "services/lib/ILogger.ts";
import { Provide } from "microdi";
import { PaginationResolver } from "services/pagination-resolver/PaginationResolver.ts";
import { RetrierHTMLClient } from "services/network/RetrierHTMLClient.ts";
import { OnPageParser } from "services/on-page-parser/OnPageParser.ts";
import { ConsoleLogger } from "services/logger/ConsoleLogger.ts";

@Provide(PaginationResolver, RetrierHTMLClient, OnPageParser, ConsoleLogger)
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
