import {
  DOMParser as DenoDOMParser,
  Element,
  HTMLDocument,
  Node,
} from "denodom";
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

  public ifNodeIsElement(node: Node, callback: (element: Element) => void) {
    const isElement = node instanceof Element;
    if (!isElement) return;
    callback(node as Element);
  }
}

provide(DOMParser, []);
