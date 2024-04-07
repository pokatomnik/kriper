import { BoundMethod } from "decorate";
import { Provide } from "microdi";
import type { IParser } from "services/lib/IParser.ts";

/**
 * Yandex API returns raw description for the story.
 * Sometimes that description inlude unwanted statements.
 * This parser removes them as well as unwanted characters.
 */
@Provide()
export class ShortDescriptionParser implements IParser<string> {
  private deleteStatements: ReadonlySet<string> = new Set([
    "•",
    "Возможность незарегистрированным пользователям писать комментарии и выставлять рейтинг временно отключена.",
    "Возможность комментирования и выставления рейтинга временно отключена для незарегистрированных пользователей.",
    "Посетители в группе Гости не могут оставлять комментарии к данной публикации.",
    "Пересказана только часть статьи. Для продолжения перейдите к чтению оригинала.",
  ]);

  @BoundMethod
  public parse(source: string): Promise<string> {
    const lines = source.split("\n");
    const contentWithRemovedStatements = lines.reduce((acc, currentLine) => {
      let updatedLine = currentLine;
      for (const statement of this.deleteStatements) {
        while (updatedLine.includes(statement)) {
          updatedLine = updatedLine.replace(statement, "");
        }
      }
      updatedLine = updatedLine.trim();
      if (updatedLine.length > 0) {
        acc.push(updatedLine);
      }
      return acc;
    }, new Array<string>());

    return Promise.resolve(contentWithRemovedStatements.join("\n"));
  }
}
