import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
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
