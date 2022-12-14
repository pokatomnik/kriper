import type { IParser } from "../lib/IParser.ts";
import type { ITagsGroupMap } from "../../domain/ITagGroupsMap.ts";
import type { ITag } from "../../domain/ITag.ts";
import { DOMParser } from "../dom-parser/DOMParser.ts";
import { provide } from "provide";
import { Element } from "denodom";

export class TagsParser implements IParser<ITagsGroupMap> {
  private static readonly CONTENT_SELECTOR = "div.fullstory-width";

  private static readonly HEADERS_SELECTOR = "h2";

  private static readonly TAGS_WRAPPER_SELECTOR = "h2 ~ p";

  private static readonly TAG_SPAN_SELECTOR = "span.tagsc > a";

  public constructor(private readonly domParser: DOMParser) {}

  private getTagGroupNames(element: Element): Array<string> {
    return this.domParser
      .querySelectAllElements(element, TagsParser.HEADERS_SELECTOR)
      .map((element) => element.innerText.trim());
  }

  private getTagsWrappers(element: Element): Array<Element> {
    return this.domParser.querySelectAllElements(
      element,
      TagsParser.TAGS_WRAPPER_SELECTOR
    );
  }

  private getTagNamesFromWrapper(tagWrapper: Element): Array<string> {
    return this.domParser
      .querySelectAllElements(tagWrapper, TagsParser.TAG_SPAN_SELECTOR)
      .map((anchor) => {
        return anchor.innerText.trim();
      });
  }

  public parse(source: string): Promise<ITagsGroupMap> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document tree");
    }

    const contentDiv = document.querySelector(TagsParser.CONTENT_SELECTOR);
    if (!contentDiv) {
      throw new Error("Failed to get content div");
    }

    const tagGroupNames = this.getTagGroupNames(contentDiv);
    const tagsGroupWrappers = this.getTagsWrappers(contentDiv);

    if (tagGroupNames.length !== tagsGroupWrappers.length) {
      throw new Error("Tags headers and tags wrappers are different");
    }

    const tagsGroupMap: ITagsGroupMap = {};
    for (let i = 0; i < tagGroupNames.length; ++i) {
      const currentGroupName = tagGroupNames[i];
      const currentTagsWrapper = tagsGroupWrappers[i];

      if (currentGroupName === undefined || currentTagsWrapper === undefined) {
        throw new Error("Index out of bounds error");
      }

      const tagNames = this.getTagNamesFromWrapper(currentTagsWrapper);

      const tagGroup: { [tagTitle: string]: ITag } = {};
      for (const tagName of tagNames) {
        const tag: ITag = { tagName: tagName, pages: [] };
        tagGroup[tagName] = tag;
      }

      tagsGroupMap[currentGroupName] = tagGroup;
    }

    return Promise.resolve(tagsGroupMap);
  }
}

provide(TagsParser, [DOMParser]);
