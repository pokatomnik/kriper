import type { IParser } from "services/lib/IParser.ts";
import type { IPagination } from "services/lib/IPagination.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class PaginationParser implements IParser<IPagination> {
  private static readonly PAGNINATION_WRAPPER_SELECTOR = "div.pages";

  private static readonly PAGE_ANCHORS_SELECTOR = "span, a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string) {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document from source string");
    }

    const paginationWrapper = document.querySelector(
      PaginationParser.PAGNINATION_WRAPPER_SELECTOR
    );
    if (!paginationWrapper) {
      throw new Error("Failed to find pagination wrapper");
    }

    const pageAnchors = this.domParser.querySelectAllElements(
      paginationWrapper,
      PaginationParser.PAGE_ANCHORS_SELECTOR
    );
    if (pageAnchors.length === 0) {
      throw new Error("Failed to find pages");
    }
    if (pageAnchors.length === 1) {
      throw new Error("Suspicious pages set. There is only one page");
    }

    const firstPageElement = pageAnchors[0];
    const lastPageElement = pageAnchors[pageAnchors.length - 1];

    if (!firstPageElement || !lastPageElement) {
      throw new Error("Missing pages behind pages indexes");
    }

    const firstPage = Number.parseInt(firstPageElement.innerText, 10);
    const lastPage = Number.parseInt(lastPageElement.innerText, 10);

    if (
      Number.isNaN(firstPage) ||
      Number.isNaN(lastPage) ||
      firstPage >= lastPage
    ) {
      throw new Error("Pages buttons have incorrect labels");
    }

    return Promise.resolve({
      minPage: firstPage,
      maxPage: lastPage,
    });
  }
}
