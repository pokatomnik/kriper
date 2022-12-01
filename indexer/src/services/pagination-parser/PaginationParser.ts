import type { IParser } from "../lib/IParser.ts";
import type { IPagination } from "../lib/IPagination.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

export class PaginationParser implements IParser<IPagination> {
  private static readonly PAGNINATION_WRAPPER_SELECTOR = "ul.pagination";

  private static readonly PAGINATION_PARTS_SELECTOR = "li";

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

    const [, pagesContainer] = this.domParser.querySelectAllElements(
      paginationWrapper,
      PaginationParser.PAGINATION_PARTS_SELECTOR
    );
    if (!pagesContainer) {
      throw new Error("Failed to find pages container");
    }

    const pageAnchors = this.domParser.querySelectAllElements(
      pagesContainer,
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

provide(PaginationParser, [DOMParser]);
