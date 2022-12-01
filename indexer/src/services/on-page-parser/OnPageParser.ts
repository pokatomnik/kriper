import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class OnPageParser implements IParser<Array<string>> {
  private static readonly PAGE_TITLE_ANCHORS_SELECTOR = "div.short-title > a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<Array<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error(
        "Failed to build pagination page document from source string"
      );
    }

    const anchors = this.domParser.querySelectAllElements(
      document,
      OnPageParser.PAGE_TITLE_ANCHORS_SELECTOR
    );

    const pageLinks = anchors.reduce((acc, anchor) => {
      const href = anchor.getAttribute("href");
      if (href) return acc.add(href);
      return acc;
    }, new Set<string>());

    return Promise.resolve(Array.from(pageLinks));
  }
}

provide(OnPageParser, [DOMParser]);
