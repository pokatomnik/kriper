import { FileReader, Snapshot } from "../../lib/TestTools.ts";
import { OnPageParser } from "./OnPageParser.ts";
import { DOMParser } from "../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test OnPageParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./OnPageParser.source.html"
    );
    const links = await new OnPageParser(new DOMParser()).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(links, null, 2),
      "./OnPageParser.snapshot.json"
    );
  }
);
