import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class SourceParser implements IParser<string | null> {
  private static readonly SOURCE_ELEMENT_SELECTOR =
    "span.text-muted span i.fa-quote-right + a";

  public constructor(private readonly domParser: DOMParser) {}

  private getURL(href: string): string | null {
    const url = new URL(href);
    if (url.protocol === "http:" || url.protocol === "https:") {
      return href;
    }
    return null;
  }

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

    const url = this.getURL(href);

    return Promise.resolve(url);
  }
}

provide(SourceParser, [DOMParser]);
