import type { IParser } from "../../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { HTMLProcessor } from "./HTMLProcessor.ts";

export class ContentParser implements IParser<string> {
  private static readonly CONTENT_CONTAINER_SELECTOR = "div.img-fix";

  public constructor(
    private readonly domParser: DOMParser,
    private readonly htmlProcessor: HTMLProcessor
  ) {}

  public parse(source: string): Promise<string> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const contentContainer = document.querySelector(
      ContentParser.CONTENT_CONTAINER_SELECTOR
    );
    if (!contentContainer) {
      throw new Error("Failed to find content container");
    }

    this.htmlProcessor.processHTML(contentContainer, document);

    const content = contentContainer.innerText
      .trim()
      // Remove spaces at the start of line
      .replace(/\n(\s)+/gi, "\n")
      // Duplicate eols for markdown
      .replaceAll("\n", "\n\n")
      // Replace short dashes with long dashes for direct speech
      .replaceAll("\n-", "\n—")
      // Replace duplicated bold text parts (see bold text improvements in the HTMLProcessor)
      .replaceAll("****", "**  **");

    return Promise.resolve(content);
  }
}

provide(ContentParser, [DOMParser, HTMLProcessor]);
