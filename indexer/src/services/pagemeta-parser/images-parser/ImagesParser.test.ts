import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { ImagesParser } from "./ImagesParser.ts";

Deno.test(
  {
    name: "Test ImagesParser",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./ImagesParser.source.html"
    );
    const images = await new ImagesParser(new DOMParser(), {
      originURL: "https://kriper.net",
    }).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      JSON.stringify(images, null, 2),
      "./ImagesParser.snapshot.json"
    );
  }
);
