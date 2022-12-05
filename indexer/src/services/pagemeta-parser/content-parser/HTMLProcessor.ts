import type { ElementHander } from "./ElementHandler.ts";
import type { IURLConfiguration } from "../../configuration/IURLConfiguration.ts";
import type { Element, Document } from "denodom";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { URLConfiguration } from "../../configuration/URLConfiguration.ts";

export class HTMLProcessor {
  private static readonly EOL = "\n";

  private readonly handlers: ReadonlyArray<ElementHander> = [
    // Remove all scripts first
    (element) => {
      if (element.tagName === "SCRIPT") {
        element.remove();
      }
    },
    // Remove all SVGs
    (element) => {
      if (element.tagName === "SVG") {
        element.remove();
      }
    },
    // Remove tags
    (element) => {
      if (
        element.tagName === "SPAN" &&
        element.classList.contains("fullstorytags")
      ) {
        element.remove();
      }
    },
    // Remove Back To Menu links
    (element) => {
      if (
        element.tagName === "A" &&
        element.getAttribute("title") === "К меню"
      ) {
        element.remove();
      }
    },
    // Add line endings to paragraphs
    (element, _, document) => {
      if (
        (element.tagName === "P" || element.tagName === "DIV") &&
        element.classList.length === 0
      ) {
        element.appendChild(document.createElement("br"));
      }
    },
    // Improve headers h1
    (element) => {
      if (element.tagName === "H1") {
        element.innerText = `\n# ${element.innerText.trim()}${
          HTMLProcessor.EOL
        }`;
      }
    },
    // Improve headers h2
    (element) => {
      if (element.tagName === "H2") {
        element.innerText = `\n## ${element.innerText.trim()}${
          HTMLProcessor.EOL
        }`;
      }
    },
    // Improve headers h3
    (element) => {
      if (element.tagName === "H3") {
        element.innerText = `\n### ${element.innerText.trim()}${
          HTMLProcessor.EOL
        }`;
      }
    },
    // Improve lists UL
    (element) => {
      if (element.tagName === "UL") {
        const liItems = this.domParser.querySelectAllElements(element, "li");
        for (const li of liItems) {
          li.innerText = `* ${li.innerText.trim()}${HTMLProcessor.EOL}`;
        }
      }
    },
    // Improve lists OL
    (element) => {
      if (element.tagName === "OL") {
        const listItems = this.domParser.querySelectAllElements(element, "li");
        for (let i = 0; i < listItems.length; ++i) {
          const li = listItems[i];
          if (li) {
            li.innerText = `${i + 1}. ${li.innerText.trim()}${
              HTMLProcessor.EOL
            }`;
          }
        }
      }
    },
    // Add Images
    (element, _, document) => {
      if (element.tagName === "IMG") {
        const biggestImageSource =
          element.parentElement?.getAttribute("href") ||
          element.getAttribute("src") ||
          element.getAttribute("data-src");

        if (!biggestImageSource) return;

        const actualSource = biggestImageSource.startsWith("http")
          ? biggestImageSource
          : this.urlConfiguration.originURL.concat(biggestImageSource);
        const spanWithMarkdown = document.createElement("span");
        spanWithMarkdown.innerText = `\n![Изображение загружается...](${actualSource})\n`;
        element.replaceWith(spanWithMarkdown);
      }
    },
    // Replace raw line endings
    (_, container) => {
      container.innerHTML = container.innerHTML
        .replaceAll("<br>", HTMLProcessor.EOL)
        .replaceAll("<br/>", HTMLProcessor.EOL)
        .replaceAll("<br />", HTMLProcessor.EOL);
    },
  ];

  public constructor(
    private readonly domParser: DOMParser,
    private readonly urlConfiguration: IURLConfiguration
  ) {}

  /**
   * Prepare HTML to extract markdown from It.
   * Warning: It can modify both `contentDiv` and `document`.
   * @param contentDiv
   * @param document
   */
  public processHTML(contentDiv: Element, document: Document) {
    const elements = contentDiv.querySelectorAll("*");
    for (const elementHandler of this.handlers) {
      for (const innerNode of elements) {
        this.domParser.ifNodeIsElement(innerNode, (innerElement) => {
          elementHandler(innerElement, contentDiv, document);
        });
      }
    }
  }
}

provide(HTMLProcessor, [DOMParser, URLConfiguration]);
