import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class TitleParser implements IParser<string> {
  private static readonly TITLE_ELEMENT_SELECTOR = "h2.card-title";

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
