import { FileReader, Snapshot } from "lib/TestTools.ts";
import { TagsParser } from "services/tags-parser/TagsParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test TagsParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./TagsParser.source.html"
    );
    const parsedTags = await new TagsParser(new DOMParser()).parse(rawHTML);

    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(parsedTags, null, 2),
      "./TagsParser.snapshot.json"
    );
  }
);
