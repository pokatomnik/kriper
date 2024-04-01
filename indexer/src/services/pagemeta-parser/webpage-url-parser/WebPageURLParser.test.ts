import { FileReader, Snapshot } from "lib/TestTools.ts";
import { WebPageURLParser } from "services/pagemeta-parser/webpage-url-parser/WebPageURLParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test WebPageURLParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./WebPageURLParser.source.html"
    );
    const webPageURL = await new WebPageURLParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      webPageURL,
      "./WebPageURLParser.snapshot.txt"
    );
  }
);
