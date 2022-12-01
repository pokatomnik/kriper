import type { IParser } from "../lib/IParser.ts";
import { provide } from "provide";
import { DOMParser } from "../dom-parser/DOMParser.ts";

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

provide(AuthorNicknameParser, [DOMParser]);
