import { FileReader, Snapshot } from "lib/TestTools.ts";
import { ContentParser } from "services/pagemeta-parser/content-parser/ContentParser.ts";
import { DOMParser } from "services/dom-parser/DOMParser.ts";
import { HTMLProcessor } from "services/pagemeta-parser/content-parser/HTMLProcessor.ts";

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
        shorterAPIURL: "https://yndxai.deno.dev",
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
        shorterAPIURL: "https://yndxai.deno.dev",
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
        shorterAPIURL: "https://yndxai.deno.dev",
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
        shorterAPIURL: "https://yndxai.deno.dev",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Ущелье_зомби.snapshot.md"
    );
  }
);

Deno.test(
  {
    name: "Test ContentParser - Мы уже мертвы",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./Мы_уже_мертвы.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
        shorterAPIURL: "https://yndxai.deno.dev",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Мы_уже_мертвы.snapshot.md"
    );
  }
);

Deno.test(
  {
    name: "Test ContentParser - Проклятие поющей девочки",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./Проклятие_поющей_девочки.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
        shorterAPIURL: "https://yndxai.deno.dev",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./Проклятие_поющей_девочки.snapshot.md"
    );
  }
);

Deno.test(
  {
    name: "Test ContentParser - SCP-507",
    permissions: { read: true, write: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./SCP-507.source.html"
    );
    const parsedContent = await new ContentParser(
      new DOMParser(),
      new HTMLProcessor(new DOMParser(), {
        originURL: "http://kriper.net",
        shorterAPIURL: "https://yndxai.deno.dev",
      })
    ).parse(rawHTML);
    await new Snapshot(import.meta).snapshotCheck(
      parsedContent,
      "./SCP-507.snapshot.md"
    );
  }
);
