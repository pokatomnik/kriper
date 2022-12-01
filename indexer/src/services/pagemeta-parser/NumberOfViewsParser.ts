import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class NumberOfViewsParser implements IParser<number> {
  private static readonly NUMBER_OF_VIEWS_ELEMENT_SELECTOR =
    "ul.list-inline li:nth-child(6)";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<number> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const numberOfViewsWrapper = document.querySelector(
      NumberOfViewsParser.NUMBER_OF_VIEWS_ELEMENT_SELECTOR
    );
    if (!numberOfViewsWrapper) {
      throw new Error("Failed to find number of views element");
    }

    const numberOfViewsStr = numberOfViewsWrapper.innerText
      .trim()
      .split("")
      .reduce((acc, currentChar) => {
        const digit = Number.parseInt(currentChar, 10);
        if (!Number.isNaN(digit)) return acc.concat(digit.toString());
        return acc;
      }, "");

    return Promise.resolve(Number.parseInt(numberOfViewsStr, 10));
  }
}

provide(NumberOfViewsParser, [DOMParser]);
