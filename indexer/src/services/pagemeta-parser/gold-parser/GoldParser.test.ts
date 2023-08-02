import { FileReader } from "../../../lib/TestTools.ts";
import { DOMParser } from "../../dom-parser/DOMParser.ts";
import { GoldParser } from "./GoldParser.ts";
import { assertEquals } from "testing";

Deno.test(
  {
    name: "Test GoldParser - is gold",
    permissions: { read: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./GoldParser1.source.html"
    );
    const isGold = await new GoldParser(new DOMParser()).parse(rawHTML);
    assertEquals(isGold, true);
  }
);

Deno.test(
  {
    name: "Test GoldParser - is not gold",
    permissions: { read: true },
  },
  async () => {
    const rawHTML = await new FileReader(import.meta).getFileContents(
      "./GoldParser0.source.html"
    );
    const isGold = await new GoldParser(new DOMParser()).parse(rawHTML);
    assertEquals(isGold, false);
  }
);
