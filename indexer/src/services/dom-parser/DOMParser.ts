import { DOMParser as DenoDOMParser, Element, HTMLDocument } from "denodom";
import { provide } from "provide";

export class DOMParser extends DenoDOMParser {
  public querySelectAllElements(
    element: Element | HTMLDocument,
    selector: string
  ): Array<Element> {
    return Array.from(element.querySelectorAll(selector)).reduce(
      (acc, node) => {
        if (node instanceof Element) {
          acc.push(node as Element);
        }
        return acc;
      },
      new Array<Element>()
    );
  }
}

provide(DOMParser, []);
