import type { IParser } from "../../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

export class RatingParser implements IParser<number> {
  private static readonly RATING_BUTTONS_WRAPPER =
    "div.bd-highlight.like span.align-bottom span";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<number> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const ratingStatsElement = document.querySelector(
      RatingParser.RATING_BUTTONS_WRAPPER
    );

    if (!ratingStatsElement) {
      return Promise.resolve(0);
    }

    const rating = Number.parseInt(ratingStatsElement.innerText.trim());
    if (Number.isNaN(rating)) {
      throw new Error(`Suspicious rating value`);
    }

    return Promise.resolve(rating);
  }
}

provide(RatingParser, [DOMParser]);
