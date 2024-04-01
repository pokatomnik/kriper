import type { IParser } from "services/lib/IParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";
import { Provide } from "microdi";

@Provide(DOMParser)
export class TopIdsParser implements IParser<ReadonlySet<string>> {
  private static readonly ITEM_CONTAINERS_SELECTOR =
    ".card.mb-3.mx-auto.card-hover.main-width ol > li";

  private static readonly ITEM_SELECTOR = "b > span";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<Set<string>> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document tree");
    }

    const idContainers = this.domParser.querySelectAllElements(
      document,
      TopIdsParser.ITEM_CONTAINERS_SELECTOR
    );

    const items = idContainers.reduce((acc, containerElement) => {
      const id = containerElement
        .querySelector(TopIdsParser.ITEM_SELECTOR)
        ?.innerText.trim();
      return id ? acc.add(id) : acc;
    }, new Set<string>());

    return Promise.resolve(items);
  }
}
