import { FileReader, Snapshot } from "lib/TestTools.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";
import { SeeAlsoParser } from "services/pagemeta-parser/see-also-parser/SeeAlsoParser.ts";

Deno.test(
  {
    name: "Test SeeAlsoParser - has see also links",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./SeeAlsoParser0.source.html"
    );
    const seeAlsoLinks = await new SeeAlsoParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(seeAlsoLinks, null, 2),
      "./SeeAlsoParser0.snapshot.json"
    );
  }
);
