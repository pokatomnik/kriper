import { FileReader } from "../../../lib/TestTools.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { StoryIdentifierParser } from "./StoryIdentifierParser.ts";
import * as Testing from "testing";

Deno.test(
  {
    name: "Test StoryIdentifierParser - get identifier",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./StoryIdentifierParser.source.html"
    );
    const storyId = await new StoryIdentifierParser(new DOMParser()).parse(
      rawHTML
    );
    Testing.assertEquals(storyId, "12324");
  }
);
