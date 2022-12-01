import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class TitleParser implements IParser<string> {
  private static readonly TITLE_ELEMENT_SELECTOR = "div.header-block h1";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<string> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const headerElement = document.querySelector(
      TitleParser.TITLE_ELEMENT_SELECTOR
    );
    if (!headerElement) {
      throw new Error("Failed to find header element");
    }

    const title = headerElement.innerText.trim();

    if (!title) {
      throw new Error("Title is empty");
    }
    return Promise.resolve(title);
  }
}

provide(TitleParser, [DOMParser]);
