import { FileReader, Snapshot } from "../../lib/TestTools.ts";
import { PaginationParser } from "./PaginationParser.ts";
import { DOMParser } from "../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test PaginationParser - first page",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./PaginationParser1.source.html"
    );
    const pagination = await new PaginationParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(pagination, null, 2),
      "./PaginationParser1.snapshot.json"
    );
  }
);

Deno.test(
  {
    name: "Test PaginationParser - second page",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./PaginationParser2.source.html"
    );
    const pagination = await new PaginationParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(pagination, null, 2),
      "./PaginationParser2.snapshot.json"
    );
  }
);

Deno.test(
  {
    name: "Test PaginationParser - last page",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./PaginationParserLastPage.source.html"
    );
    const pagination = await new PaginationParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(pagination, null, 2),
      "./PaginationParserLastPage.snapshot.json"
    );
  }
);
