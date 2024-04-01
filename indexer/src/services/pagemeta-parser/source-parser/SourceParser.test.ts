import { FileReader, Snapshot } from "lib/TestTools.ts";
import { SourceParser } from "services/pagemeta-parser/source-parser/SourceParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

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

Deno.test(
  {
    name: "Test SourceParser - source starts with domain name, www...",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./SourceParser2.source.html"
    );
    const source = await new SourceParser(new DOMParser()).parse(rawHTML);

    await new Snapshot(import.meta).snapshotCheck(
      String(source),
      "./SourceParser2.snapshot.txt"
    );
  }
);

Deno.test(
  {
    name: "Test SourceParser - source has incorrect chars",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./SourceParser3.source.html"
    );
    const source = await new SourceParser(new DOMParser()).parse(rawHTML);

    await new Snapshot(import.meta).snapshotCheck(
      String(source),
      "./SourceParser3.snapshot.txt"
    );
  }
);
