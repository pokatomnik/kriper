import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class SeeAlsoParser implements IParser<ReadonlyArray<string>> {
  // TODO Bad selector, try better one
  private static readonly SEEALSO_LINKS_SELECTOR = "div.card-body > li > a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<Array<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const seeAlsoLinks = this.domParser.querySelectAllElements(
      document,
      SeeAlsoParser.SEEALSO_LINKS_SELECTOR
    );

    const seeAlsoNames = seeAlsoLinks.reduce((acc, current) => {
      const title = current.getAttribute("title");
      if (!title) return acc;
      return acc.add(title);
    }, new Set<string>());

    return Promise.resolve(Array.from(seeAlsoNames));
  }
}
