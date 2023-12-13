import { FileReader, Snapshot } from "../../lib/TestTools.ts";
import { NY2024StoriesParser } from "./NY2024StoriesParser.ts";
import { DOMParser } from "../dom-parser/DOMParser.ts";

Deno.test(
  {
    name: "Test NY2024StoriesParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./NY2024StoriesParser.source.html"
    );
    const links = await new NY2024StoriesParser(new DOMParser()).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(links, null, 2),
      "./NY2024StoriesParser.snapshot.json"
    );
  }
);
