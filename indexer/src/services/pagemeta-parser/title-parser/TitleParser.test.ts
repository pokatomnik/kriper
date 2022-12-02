import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { TitleParser } from "./TitleParser.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test TitleParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./TitleParser.source.html"
    );
    const title = await new TitleParser(new DOMParser()).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      title,
      "./TitleParser.snapshot.txt"
    );
  }
);
