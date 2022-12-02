import type { IParser } from "../../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

export class TagsParser implements IParser<ReadonlyArray<string>> {
  private static readonly TAGS_WRAPPER_SELECTOR = "span.fullstorytags";

  private static readonly TAG_SELECTOR = "a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<ReadonlyArray<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const tagsWrapper = document.querySelector(
      TagsParser.TAGS_WRAPPER_SELECTOR
    );

    if (!tagsWrapper) {
      return Promise.resolve([]);
    }

    const tagWrappers = this.domParser.querySelectAllElements(
      tagsWrapper,
      TagsParser.TAG_SELECTOR
    );
    const tags = tagWrappers.map((anchor) => {
      return anchor.innerText.trim();
    });

    return Promise.resolve(tags);
  }
}

provide(TagsParser, [DOMParser]);
