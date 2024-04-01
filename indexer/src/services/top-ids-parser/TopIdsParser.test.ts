import { FileReader } from "lib/TestTools.ts";
import { TopIdsParser } from "services/top-ids-parser/TopIdsParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";
import * as Testing from "testing";

Deno.test(
  {
    name: "Test TopIdsParser - has elements",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./TopIdsParser0.source.html"
    );
    const parsedIds = await new TopIdsParser(new DOMParser()).parse(rawHTML);

    Testing.assertEquals(parsedIds, new Set(["12389", "12383"]));
  }
);
