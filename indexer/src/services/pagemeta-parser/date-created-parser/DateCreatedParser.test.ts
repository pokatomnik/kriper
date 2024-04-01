import { FileReader, Snapshot } from "lib/TestTools.ts";
import { DateCreatedParser } from "services/pagemeta-parser/date-created-parser/DateCreatedParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";
import { assertEquals } from "testing";

Deno.test(
  {
    name: "Test DateCreatedParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./DateCreatedParser0.source.html"
    );
    const dateCreated = await new DateCreatedParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(dateCreated, null, 2),
      "./DateCreatedParser0.snapshot.json"
    );
  }
);

Deno.test(
  {
    name: "Test DateCreatedParser - check Yesterday",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./DateCreatedParser1.source.html"
    );
    const dateCreated = await new DateCreatedParser(new DOMParser()).parse(
      rawHTML
    );
    const yesterday = new Date(Date.now() - 1000 * 60 * 60 * 24);
    assertEquals(dateCreated, {
      day: yesterday.getDate(),
      month: yesterday.getMonth() + 1,
      year: yesterday.getFullYear(),
    });
  }
);

Deno.test(
  {
    name: "Test DateCreatedParser - check Today",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./DateCreatedParser2.source.html"
    );
    const dateCreated = await new DateCreatedParser(new DOMParser()).parse(
      rawHTML
    );
    const yesterday = new Date();
    assertEquals(dateCreated, {
      day: yesterday.getDate(),
      month: yesterday.getMonth() + 1,
      year: yesterday.getFullYear(),
    });
  }
);
