import { getURL } from "services/pagemeta-parser/source-parser/URLParser.ts";
import { assertEquals } from "testing";

Deno.test("getURL - test correct URL", () => {
  const rawURL = "https://example.com";
  const url = getURL(rawURL);
  assertEquals(url, rawURL);
});

Deno.test("getURL - only domain", () => {
  const rawURL = "www.example.com";
  const url = getURL(rawURL);
  assertEquals(url, "http://www.example.com");
});

Deno.test("getURL - javascript links", () => {
  const rawURL = "javascript:console.log('no way!')";
  const url = getURL(rawURL);
  assertEquals(url, null);
});

Deno.test("getURL - weird text instead of URL", () => {
  const rawURL = "Страшные Истории ☠ Lucifera ☠";
  const url = getURL(rawURL);
  assertEquals(url, null);
});
