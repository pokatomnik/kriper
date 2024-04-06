import * as Testing from "testing";
import { ShortDescriptionParser } from "services/short-description-parser/ShortDescriptionParser.ts";

Deno.test("ShortDescriptionParser - remove unwanted characters", async () => {
  const parser = new ShortDescriptionParser();
  const text = `
    Возможность незарегистрированным пользователям писать комментарии и выставлять рейтинг временно отключена.
    • a string with bullet
    •another string with bullet
    another string without bullet
    •

    `.trim();
  const expectedContent = `
a string with bullet
another string with bullet
another string without bullet
`.trim();

  const updatedContent = await parser.parse(text);
  Testing.assertEquals(updatedContent, expectedContent);
});
