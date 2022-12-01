import type { ITagsGroupMap } from "./domain/ITagGroupsMap.ts";
import type { IClient } from "./services/lib/IClient.ts";
import { provide } from "provide";
import { TagsClient } from "./services/tags-client/TagsClient.ts";
import { PagnationClient } from "./services/pagination-client/PaginationClient.ts";
import { IPagination } from "./services/lib/IPagination.ts";

export class App {
  public constructor(
    private readonly tagsClient: IClient<ITagsGroupMap, []>,
    private readonly paginationClient: IClient<IPagination, []>
  ) {}

  public async start() {
    const tags = await this.tagsClient.get();
    const pagination = await this.paginationClient.get();
    console.log(tags);
    console.log(pagination);
  }
}

provide(App, [TagsClient, PagnationClient]);
