import type { IParser } from "../lib/IParser.ts";
import type { IRating } from "../../domain/IRating.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class RatingParser implements IParser<IRating | null> {
  private static readonly RATING_BUTTONS_WRAPPER =
    "div.card-footer span.rate_like";

  public constructor(private readonly domParser: DOMParser) {}

  private extractDigits(source: string) {
    return source
      .split("")
      .filter((char) => !Number.isNaN(Number.parseInt(char, 10)))
      .join("");
  }

  private parseLikeAndDislike(source: string): [like: number, dislike: number] {
    if (!source.includes("/")) {
      throw new Error("Suspicios rating string");
    }
    const [part0, part1] = source.split("/");
    if (!part0 || !part1) {
      throw new Error("Suspicious rating parts");
    }

    const like = Number.parseInt(this.extractDigits(part0));
    const dislike = Number.parseInt(this.extractDigits(part1));

    if (Number.isNaN(like) || Number.isNaN(dislike)) {
      throw new Error("Failed to parse like and dislike");
    }

    return [like, dislike];
  }

  public parse(source: string): Promise<IRating | null> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const [, ratingStatsElement] = this.domParser.querySelectAllElements(
      document,
      RatingParser.RATING_BUTTONS_WRAPPER
    );

    if (!ratingStatsElement) {
      return Promise.resolve(null);
    }

    const title = ratingStatsElement.getAttribute("title");
    if (!title) {
      return Promise.resolve(null);
    }
    const [like, dislike] = this.parseLikeAndDislike(title);
    const total = Number.parseInt(ratingStatsElement.innerText.trim());
    if (Number.isNaN(total)) {
      return Promise.resolve(null);
    }

    return Promise.resolve({
      total,
      bad: dislike,
      good: like,
    });
  }
}

provide(RatingParser, [DOMParser]);
