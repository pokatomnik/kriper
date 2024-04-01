import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class VideosParser implements IParser<ReadonlyArray<string>> {
  private static readonly CONTENT_CONTAINER_SELECTOR = "div.card-body.mt-2";

  private static readonly IFRAME_SELECTOR = "iframe";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<ReadonlyArray<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const contentContainer = document.querySelector(
      VideosParser.CONTENT_CONTAINER_SELECTOR
    );
    if (!contentContainer) {
      throw new Error("Failed to find content container");
    }

    const iframeElements = this.domParser.querySelectAllElements(
      contentContainer,
      VideosParser.IFRAME_SELECTOR
    );

    const videosSource = iframeElements.reduce((acc, iframe) => {
      const src = iframe.getAttribute("data-src");
      if (!src) return acc;

      const srcURL = new URL(src);
      if (!srcURL.host.includes("youtube")) return acc;

      return acc.add(src);
    }, new Set<string>());

    return Promise.resolve(Array.from(videosSource));
  }
}
