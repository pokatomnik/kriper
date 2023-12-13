import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class NY2024StoriesParser implements IParser<ReadonlyArray<string>> {
  private static readonly NY2024_STORIES_ANCHORS_SELECTOR =
    ".storyfull ul li a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<ReadonlyArray<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error(
        "Failed to build new year stories document from source string"
      );
    }

    const pageLinks = this.domParser.querySelectAllElements(
      document,
      NY2024StoriesParser.NY2024_STORIES_ANCHORS_SELECTOR
    );

    const ny2024StoryURLSet = pageLinks.reduce<Set<string>>((acc, current) => {
      const url = current.getAttribute("href");
      if (url) {
        acc.add(url);
      }
      return acc;
    }, new Set<string>());

    return Promise.resolve(Array.from(ny2024StoryURLSet));
  }
}

provide(NY2024StoriesParser, [DOMParser]);
