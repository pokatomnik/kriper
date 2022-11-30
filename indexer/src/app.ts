import { provide } from "provide";
import type { ITagsGroupMap } from "./domain/ITagGroupsMap.ts";
import type { IClient } from "./services/lib/IClient.ts";
import { TagsClient } from "./services/tags-client/TagsClient.ts";

export class App {
  public constructor(private readonly tagsClient: IClient<ITagsGroupMap, []>) {}

  public async start() {
    const tags = await this.tagsClient.get();
    console.log(tags);
  }
}

provide(App, [TagsClient]);
