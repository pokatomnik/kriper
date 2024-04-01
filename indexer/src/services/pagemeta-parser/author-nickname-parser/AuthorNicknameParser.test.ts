import { FileReader, Snapshot } from "lib/TestTools.ts";
import { AuthorNicknameParser } from "services/pagemeta-parser/author-nickname-parser/AuthorNicknameParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test AuthorNicknameParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./AuthorNicknameParser.source.html"
    );
    const title = await new AuthorNicknameParser(new DOMParser()).parse(
      rawHTML
    );
    await new Snapshot(import.meta).snapshotCheck(
      title,
      "./AuthorNicknameParser.snapshot.txt"
    );
  }
);
