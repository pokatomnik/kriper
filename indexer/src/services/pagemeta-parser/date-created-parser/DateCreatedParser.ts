import type { IParser } from "services/lib/IParser.ts";
import type { IUncheckedDate } from "domain/IUncheckedDate.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class DateCreatedParser implements IParser<IUncheckedDate> {
  private static readonly DATE_CREATED_WRAPPER_SELECTOR = "i.fa-calendar-days";

  public constructor(private readonly domParser: DOMParser) {}

  private getUncheckedDateFromString(rawDateString: string): IUncheckedDate {
    if (rawDateString.toLocaleLowerCase().includes("сегодня")) {
      const today = new Date();
      return {
        day: today.getDate(),
        month: today.getMonth() + 1,
        year: today.getFullYear(),
      };
    }

    if (rawDateString.toLocaleLowerCase().includes("вчера")) {
      const yesterday = new Date(Date.now() - 1000 * 60 * 60 * 24);
      return {
        day: yesterday.getDate(),
        month: yesterday.getMonth() + 1,
        year: yesterday.getFullYear(),
      };
    }

    const rawDateContent = rawDateString.split(",")[0] ?? "";
    const [dayStr, monthStr, yearStr] = rawDateContent.split("-");
    if (!yearStr || !monthStr || !dayStr) {
      throw new Error(
        `Suspicious date: ${rawDateContent}. Some date values are missing.`
      );
    }

    const [day, month, year] = [
      Number.parseInt(dayStr, 10),
      Number.parseInt(monthStr, 10),
      Number.parseInt(yearStr, 10),
    ];
    if (Number.isNaN(day) || Number.isNaN(month) || Number.isNaN(year)) {
      throw new Error(
        `Suspicious date: some date values are not numbers. Source string: ${rawDateString}`
      );
    }

    return { day, month, year };
  }

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

    const date = this.getUncheckedDateFromString(
      dateCreatedElement.innerText.trim()
    );

    return Promise.resolve(date);
  }
}
