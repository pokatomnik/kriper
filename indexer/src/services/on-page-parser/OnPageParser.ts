import type { IParser } from "services/lib/IParser.ts";
import type { IFetchPageParams } from "services/lib/IFetchPageParams.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class OnPageParser implements IParser<Array<IFetchPageParams>> {
  private static readonly PAGE_TITLE_ANCHORS_SELECTOR = "h2.card-title";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<Array<IFetchPageParams>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error(
        "Failed to build pagination page document from source string"
      );
    }

    const headers = this.domParser.querySelectAllElements(
      document,
      OnPageParser.PAGE_TITLE_ANCHORS_SELECTOR
    );

    const pageLinks = headers.reduce((acc, header) => {
      const href = header.parentElement?.getAttribute("href");
      const title = header.innerText.trim();
      if (!title || !href) return acc;
      return acc.set(href, {
        title,
        url: href,
      });
    }, new Map<string, IFetchPageParams>());

    return Promise.resolve(Array.from(pageLinks.values()));
  }
}
