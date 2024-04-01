import { FileReader, Snapshot } from "lib/TestTools.ts";
import { AuthorRealNameParser } from "services/pagemeta-parser/author-realname-parser/AuthorRealNameParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test AuthorRealNameParser - with real name",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./WithRealName.source.html"
    );
    const realName = await new AuthorRealNameParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      String(realName),
      "./WithRealName.snapshot.txt"
    );
  }
);

Deno.test(
  {
    name: "Test AuthorRealNameParser - without real name",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./WithoutRealName.source.html"
    );
    const realName = await new AuthorRealNameParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      String(realName),
      "./WithoutRealName.snapshot.txt"
    );
  }
);
