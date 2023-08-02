import type { IParser } from "../../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

export class GoldParser implements IParser<boolean> {
  private static readonly GOLD_MARK_SELECTOR = "span.icon_goldf";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<boolean> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }
    const goldElement = document.querySelector(GoldParser.GOLD_MARK_SELECTOR);
    return Promise.resolve(Boolean(goldElement));
  }
}

provide(GoldParser, [DOMParser]);
