import type { ITagsGroupMap } from "./domain/ITagGroupsMap.ts";
import type { IClient } from "./services/lib/IClient.ts";
import type { IPagination } from "./services/lib/IPagination.ts";
import { provide } from "provide";
import { TagsClient } from "./services/tags-client/TagsClient.ts";
import { PagnationClient } from "./services/pagination-client/PaginationClient.ts";
import { PageListClient } from "./services/pages-list-client/PagesListClient.ts";
import { IFetchPageParams } from "./services/lib/IFetchPageParams.ts";

export class App {
  public constructor(
    private readonly tagsClient: IClient<ITagsGroupMap, []>,
    private readonly paginationClient: IClient<IPagination, []>,
    private readonly pageListClient: IClient<
      Array<IFetchPageParams>,
      [IPagination]
    >
  ) {}

  public async start() {
    const tags = await this.tagsClient.get();
    const pagination = await this.paginationClient.get();
    const pagesToFetch = await this.pageListClient.get(pagination);
    console.log(tags);
    console.log(pagination);
    console.log(pagesToFetch);
  }
}

provide(App, [TagsClient, PagnationClient, PageListClient]);
