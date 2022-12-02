import type { IParser } from "../../lib/IParser.ts";
import type { IUncheckedDate } from "../../../domain/IUncheckedDate.ts";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

export class DateCreatedParser implements IParser<IUncheckedDate> {
  private static readonly DATE_CREATED_WRAPPER_SELECTOR = "i.fa-calendar-days";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<IUncheckedDate> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const dateCreatedElement = document.querySelector(
      DateCreatedParser.DATE_CREATED_WRAPPER_SELECTOR
    )?.parentElement;
    if (!dateCreatedElement) {
      throw new Error("Missing date creation element");
    }

    const rawDateContent =
      dateCreatedElement.innerText.trim().split(",")[0] ?? "";
    const [day, month, year] = rawDateContent.split("-");
    if (!year || !month || !day) {
      throw new Error(`Suspicious date: ${rawDateContent}. Can't parse this`);
    }

    return Promise.resolve({
      day: Number.parseInt(day, 10),
      month: Number.parseInt(month, 10),
      year: Number.parseInt(year, 10),
    });
  }
}

provide(DateCreatedParser, [DOMParser]);
