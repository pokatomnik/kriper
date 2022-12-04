import type { IClient } from "../lib/IClient.ts";
import type { IFetchPageParams } from "../lib/IFetchPageParams.ts";
import type { IParser } from "../lib/IParser.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import type { IPageMeta } from "../../domain/IPageMeta.ts";
import { provide } from "provide";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { PageMetaParser } from "../pagemeta-parser/PageMetaParser.ts";

export class PageMetaClient
  implements
    IClient<ReadonlyArray<IPageMeta>, [ReadonlyArray<IFetchPageParams>]>
{
  public constructor(
    private readonly httpClient: IHTMLClient,
    private readonly pageMetaParser: IParser<IPageMeta>
  ) {}

  public async get(
    fetchParamsList: ReadonlyArray<IFetchPageParams>
  ): Promise<ReadonlyArray<IPageMeta>> {
    const pageMetaList: Array<IPageMeta> = [];
    for (const fetchParams of fetchParamsList) {
      const rawHTML = await this.httpClient.get(fetchParams.url);
      const pageMeta = await this.pageMetaParser.parse(rawHTML);
      pageMetaList.push(pageMeta);
    }

    return pageMetaList;
  }
}

provide(PageMetaClient, [RetrierHTMLClient, PageMetaParser]);
