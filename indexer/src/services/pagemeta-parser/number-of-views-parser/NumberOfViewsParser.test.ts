import { FileReader, Snapshot } from "lib/TestTools.ts";
import { NumberOfViewsParser } from "services/pagemeta-parser/number-of-views-parser/NumberOfViewsParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test NumberOfViewsParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./NumberOfViewsParser.source.html"
    );
    const numberOfViews = await new NumberOfViewsParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      numberOfViews.toString(),
      "./NumberOfViewsParser.snapshot.txt"
    );
  }
);
