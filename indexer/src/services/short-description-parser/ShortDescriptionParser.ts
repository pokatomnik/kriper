import { BoundMethod } from "decorate";
import { Provide } from "microdi";
import type { IParser } from "services/lib/IParser.ts";

@Provide()
export class ShortDescriptionParser implements IParser<string> {
  private deleteStatements: ReadonlySet<string> = new Set([
    "•",
    "Возможность незарегистрированным пользователям писать комментарии и выставлять рейтинг временно отключена.",
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
