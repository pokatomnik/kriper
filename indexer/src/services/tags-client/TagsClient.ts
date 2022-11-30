import type { IClient } from "../lib/IClient.ts";
import type { ITagsGroupMap } from "../../domain/ITagGroupsMap.ts";
import type { IURLResolver } from "../lib/IURLResolver.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import type { IParser } from "../lib/IParser.ts";
import { TagsResolver } from "../tags-resolver/TagsResolver.ts";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { TagsParser } from "../tags-parser/TagsParser.ts";
import { provide } from "provide";

export class TagsClient implements IClient<ITagsGroupMap, []> {
  public constructor(
    private readonly tagsResolver: IURLResolver<[]>,
    private readonly htmlClient: IHTMLClient,
    private readonly tagsParser: IParser<ITagsGroupMap>
  ) {}

  public async get(): Promise<ITagsGroupMap> {
    const tagsUrl = this.tagsResolver.resolve();
    const tagsHTML = await this.htmlClient.get(tagsUrl);
    const tagGroups = await this.tagsParser.parse(tagsHTML);

    return tagGroups;
  }
}

provide(TagsClient, [TagsResolver, RetrierHTMLClient, TagsParser]);
