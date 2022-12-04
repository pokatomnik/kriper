import type { ITagsGroupMap } from "./domain/ITagGroupsMap.ts";
import type { IClient } from "./services/lib/IClient.ts";
import type { IPagination } from "./services/lib/IPagination.ts";
import type { IFetchPageParams } from "./services/lib/IFetchPageParams.ts";
import type { IPageMeta } from "./domain/IPageMeta.ts";
import { provide } from "provide";
import { TagsClient } from "./services/tags-client/TagsClient.ts";
import { PagnationClient } from "./services/pagination-client/PaginationClient.ts";
import { PageListClient } from "./services/pages-list-client/PagesListClient.ts";
import { PageMetaClient } from "./services/pagemeta-client/PageMetaClient.ts";

export class App {
  public constructor(
    private readonly tagsClient: IClient<ITagsGroupMap, []>,
    private readonly paginationClient: IClient<IPagination, []>,
    private readonly pageListClient: IClient<
      Array<IFetchPageParams>,
      [IPagination]
    >,
    private readonly pageMetaClient: IClient<
      ReadonlyArray<IPageMeta>,
      [ReadonlyArray<IFetchPageParams>]
    >
  ) {}

  public async start() {
    const tags = await this.tagsClient.get();
    const pagination = await this.paginationClient.get();
    const pagesToFetch = await this.pageListClient.get(pagination);
    const pageMeta = await this.pageMetaClient.get(pagesToFetch);
  }
}

provide(App, [TagsClient, PagnationClient, PageListClient, PageMetaClient]);
