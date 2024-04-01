import type { IClient } from "services/lib/IClient.ts";
import type { ITagsGroupMap } from "domain/ITagGroupsMap.ts";
import type { IURLResolver } from "services/lib/IURLResolver.ts";
import type { IHTMLClient } from "services/network/IHTMLClient.ts";
import type { IParser } from "services/lib/IParser.ts";
import { TagsResolver } from "services/tags-resolver/TagsResolver.ts";
import { RetrierHTMLClient } from "services/network/RetrierHTMLClient.ts";
import { TagsParser } from "services/tags-parser/TagsParser.ts";
import { Provide } from "microdi";

@Provide(TagsResolver, RetrierHTMLClient, TagsParser)
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
