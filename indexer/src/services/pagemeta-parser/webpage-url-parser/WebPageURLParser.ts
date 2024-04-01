import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class WebPageURLParser implements IParser<string> {
  private static readonly WEBPAGE_URL_LINK_SELECTOR = 'link[rel="canonical"]';

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<string> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const webpageURLLink = document.querySelector(
      WebPageURLParser.WEBPAGE_URL_LINK_SELECTOR
    );
    if (!webpageURLLink) {
      throw new Error("Failed to find canonical webpage url");
    }

    const webpageURL = webpageURLLink.getAttribute("href");
    if (!webpageURL) {
      throw new Error("Canonical webpage url is empty");
    }

    return Promise.resolve(webpageURL);
  }
}
