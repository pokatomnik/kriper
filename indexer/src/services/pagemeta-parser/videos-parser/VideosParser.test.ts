import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { VideosParser } from "./VideosParser.ts";

Deno.test(
  {
    name: "Test VideosParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./VideosParser.source.html"
    );
    const videos = await new VideosParser(new DOMParser()).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(videos, null, 2),
      "./VideosParser.snapshot.json"
    );
  }
);
