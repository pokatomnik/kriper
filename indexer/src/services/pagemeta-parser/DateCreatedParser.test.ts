import { FileReader, Snapshot } from "../../lib/TestTools.ts";
import { DateCreatedParser } from "./DateCreatedParser.ts";
import { DOMParser } from "../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test DateCreatedParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./DateCreatedParser.source.html"
    );
    const dateCreated = await new DateCreatedParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(dateCreated, null, 2),
      "./DateCreatedParser.snapshot.json"
    );
  }
);
