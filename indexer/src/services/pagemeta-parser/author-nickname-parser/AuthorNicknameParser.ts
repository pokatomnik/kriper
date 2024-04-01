import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

@Provide(DOMParser)
export class AuthorNicknameParser implements IParser<string> {
  private static readonly AUTHOR_NICKNAME_WRAPPER_SELECTOR = "i.fa-user ~ a";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<string> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const authorNicknameElement = document.querySelector(
      AuthorNicknameParser.AUTHOR_NICKNAME_WRAPPER_SELECTOR
    );
    if (!authorNicknameElement) {
      throw new Error("Failed to find user nickname");
    }

    const nickname = authorNicknameElement.innerText.trim();

    if (!nickname) {
      throw new Error("Nickname is empty");
    }

    return Promise.resolve(nickname);
  }
}
