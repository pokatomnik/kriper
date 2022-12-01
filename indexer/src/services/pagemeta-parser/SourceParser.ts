import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class SourceParser implements IParser<string | null> {
  private static readonly SOURCE_ELEMENT_SELECTOR =
    "div.card-footer span.linka > a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<string | null> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const sourceElement = document.querySelector(
      SourceParser.SOURCE_ELEMENT_SELECTOR
    );
    if (!sourceElement) {
      return Promise.resolve(null);
    }

    const href = sourceElement.getAttribute("href");
    if (!href) {
      return Promise.resolve(null);
    }

    return Promise.resolve(href);
  }
}

provide(SourceParser, [DOMParser]);
