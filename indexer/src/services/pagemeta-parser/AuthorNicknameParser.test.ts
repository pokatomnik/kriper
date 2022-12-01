import { FileReader, Snapshot } from "../../lib/TestTools.ts";
import { AuthorNicknameParser } from "./AuthorNicknameParser.ts";
import { DOMParser } from "../dom-parser/DOMParser.ts";

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
