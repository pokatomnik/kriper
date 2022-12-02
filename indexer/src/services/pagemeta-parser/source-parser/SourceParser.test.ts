import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { SourceParser } from "./SourceParser.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test SourceParser - source exist",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./SourceParser0.source.html"
    );
    const source = await new SourceParser(new DOMParser()).parse(rawHTML);

    await new Snapshot(import.meta).snapshotCheck(
      String(source),
      "./SourceParser0.snapshot.txt"
    );
  }
);

Deno.test(
  {
    name: "Test SourceParser - source is missing",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./SourceParser1.source.html"
    );
    const source = await new SourceParser(new DOMParser()).parse(rawHTML);

    await new Snapshot(import.meta).snapshotCheck(
      String(source),
      "./SourceParser1.snapshot.txt"
    );
  }
);
