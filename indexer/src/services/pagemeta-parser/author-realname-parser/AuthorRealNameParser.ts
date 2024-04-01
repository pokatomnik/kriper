import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class AuthorRealNameParser implements IParser<string | null> {
  private static readonly AUTHOR_REALNAME_WRAPPER_SELECTOR =
    "div.card-body.mt-2 a";

  private static REALNAME_NOT_SET_CONTENT = "Указать автора";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<string | null> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const authorRealNameElement = document.querySelector(
      AuthorRealNameParser.AUTHOR_REALNAME_WRAPPER_SELECTOR
    );
    if (!authorRealNameElement) {
      return Promise.resolve(null);
    }

    const realName = authorRealNameElement.innerText.trim();

    if (!realName) {
      return Promise.resolve(null);
    }

    if (
      realName
        .toLocaleLowerCase()
        .includes(
          AuthorRealNameParser.REALNAME_NOT_SET_CONTENT.toLocaleLowerCase()
        )
    ) {
      return Promise.resolve(null);
    }

    return Promise.resolve(realName);
  }
}
