import { DOMParser } from "services/dom-parser/DOMParser.ts";
import type { IParser } from "services/lib/IParser.ts";
import { Provide } from "microdi";

@Provide(DOMParser)
export class StoryIdentifierParser implements IParser<string> {
  private static readonly STORY_ID_SPAN_ID = "story_id";

  public constructor(private readonly domParser: DOMParser) {}

  public parse(source: string): Promise<string> {
    const document = this.domParser.parseFromString(source, "text/html");
    if (!document) {
      throw new Error("Failed to build document based on input string");
    }

    const storyIdSpan = document.getElementById(
      StoryIdentifierParser.STORY_ID_SPAN_ID
    );

    if (!storyIdSpan) {
      throw new Error("Story identifier span not found");
    }

    const storyId = storyIdSpan.innerText.trim();

    return Promise.resolve(storyId);
  }
}
