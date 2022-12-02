import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { ReadingTimeMinutesParser } from "./ReadingTimeMinutesParser.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test ReadingTimeMinutesParser with fraction",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./ReadingTimeMinutesParser0.source.html"
    );
    const readingTime = await new ReadingTimeMinutesParser(
      new DOMParser()
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      readingTime.toString(),
      "./ReadingTimeMinutesParser0.snapshot.txt"
    );
  }
);

Deno.test(
  {
    name: "Test ReadingTimeMinutesParser with no fraction",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./ReadingTimeMinutesParser1.source.html"
    );
    const readingTime = await new ReadingTimeMinutesParser(
      new DOMParser()
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      readingTime.toString(),
      "./ReadingTimeMinutesParser1.snapshot.txt"
    );
  }
);
