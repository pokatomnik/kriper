import { TopTypeResolver } from "./TopTypeResolver.ts";
import { assertEquals } from "testing";

Deno.test("Test TopTypeResolver - week top", () => {
  const topTypeResolver = new TopTypeResolver({
    originURL: "https://kriper.net",
  });

  const url = topTypeResolver.resolve("weekTop");

  assertEquals(url, "https://kriper.net/top/top-week/");
});

Deno.test("Test TopTypeResolver - month top", () => {
  const topTypeResolver = new TopTypeResolver({
    originURL: "https://kriper.net",
  });

  const url = topTypeResolver.resolve("monthTop");

  assertEquals(url, "https://kriper.net/top/top-month/");
});

Deno.test("Test TopTypeResolver - year top", () => {
  const topTypeResolver = new TopTypeResolver({
    originURL: "https://kriper.net",
  });

  const url = topTypeResolver.resolve("yearTop");

  assertEquals(url, "https://kriper.net/top/top-year/");
});

Deno.test("Test TopTypeResolver - all the time top", () => {
  const topTypeResolver = new TopTypeResolver({
    originURL: "https://kriper.net",
  });

  const url = topTypeResolver.resolve("allTheTimeTop");

  assertEquals(url, "https://kriper.net/top/top-alltime/");
});
