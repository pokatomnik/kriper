import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class ReadingTimeMinutesParser implements IParser<number> {
  private static readonly READING_TIME_ELEMENT_SELECTOR =
    "span.text-muted span i.fa-clock";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<number> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const readingTimeWrapper = document.querySelector(
      ReadingTimeMinutesParser.READING_TIME_ELEMENT_SELECTOR
    )?.parentElement;
    if (!readingTimeWrapper) {
      throw new Error("Failed to find number of views element");
    }

    const numberOfViewsStr = readingTimeWrapper.innerText
      .trim()
      .replace(",", ".")
      .split("")
      .filter((char) => {
        return (
          !Number.isNaN(Number.parseInt(char)) || char === "." || char === ","
        );
      })
      .join("");
    const [int, frac] = numberOfViewsStr.split(".").filter(Boolean);
    const numStr = [int, frac].filter(Boolean).join(".");

    return Promise.resolve(Number.parseFloat(numStr));
  }
}

provide(ReadingTimeMinutesParser, [DOMParser]);
