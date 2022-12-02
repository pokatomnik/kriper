import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { TagsParser } from "./TagsParser.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test TagsParser - has tags",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./TagsParser0.source.html"
    );
    const tags = await new TagsParser(new DOMParser()).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(tags, null, 2),
      "./TagsParser0.snapshot.json"
    );
  }
);

Deno.test(
  {
    name: "Test TagsParser - has no tags",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./TagsParser1.source.html"
    );
    const tags = await new TagsParser(new DOMParser()).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(tags, null, 2),
      "./TagsParser1.snapshot.json"
    );
  }
);
