import { FileReader, Snapshot } from "../../../lib/TestTools.ts";
import { ContentParser } from "./ContentParser.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { HTMLProcessor } from "./HTMLProcessor.ts";

Deno.test(
  {
    name: "Test ContentParser - Гоголево",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./Гоголево.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Гоголево.snapshot.md"
    );
  }
);

Deno.test(
  {
    name: "Test ContentParser - Курочка",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./Курочка.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Курочка.snapshot.md"
    );
  }
);

Deno.test(
  {
    name: "Test ContentParser - Снежить",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./Снежить.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Снежить.snapshot.md"
    );
  }
);

Deno.test(
  {
    name: "Test ContentParser - Ущелье зомби",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./Ущелье_зомби.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Ущелье_зомби.snapshot.md"
    );
  }
);
