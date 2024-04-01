import { FileReader, Snapshot } from "lib/TestTools.ts";
import { RatingParser } from "services/pagemeta-parser/rating-parser/RatingParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test RatingParser with rating block",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./RatingParser0.source.html"
    );
    const rating = await new RatingParser(new DOMParser()).parse(rawHTML);

    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(rating, null, 2),
      "./RatingParser0.snapshot.txt"
    );
  }
);
