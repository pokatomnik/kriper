import type { IParser } from "../../lib/IParser.ts";
import type { IURLConfiguration } from "../../configuration/IURLConfiguration.ts";
import { provide } from "provide";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { URLConfiguration } from "../../configuration/URLConfiguration.ts";

export class ImagesParser implements IParser<ReadonlyArray<string>> {
  private static readonly CONTENT_CONTAINER_SELECTOR =
    "div.card-body.mt-2 > div";

  private static readonly IMAGE_SELECTOR = "img";

  public constructor(
    private readonly domParser: DOMParser,
    private readonly urlConfiguration: IURLConfiguration
  ) {}

  public parse(source: string): Promise<ReadonlyArray<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const contentContainer = document.querySelector(
      ImagesParser.CONTENT_CONTAINER_SELECTOR
    );

    if (!contentContainer) {
      throw new Error("Failed to find content container");
    }

    const imageElements = this.domParser.querySelectAllElements(
      contentContainer,
      ImagesParser.IMAGE_SELECTOR
    );

    const imagesSource = imageElements.reduce((acc, image) => {
      const biggestImageSource =
        image.parentElement?.getAttribute("href") ||
        image.getAttribute("src") ||
        image.getAttribute("data-src");
      if (!biggestImageSource) return acc;

      const actualSource = biggestImageSource.startsWith("http")
        ? biggestImageSource
        : this.urlConfiguration.originURL.concat(biggestImageSource);

      return acc.add(actualSource);
    }, new Set<string>());

    return Promise.resolve(Array.from(imagesSource));
  }
}

provide(ImagesParser, [DOMParser, URLConfiguration]);
