import { StoriesPaginationResolver } from "./StoriesPaginationResolver.ts";
import { assertEquals, assertThrows } from "testing";

Deno.test("Test StoriesPaginationResolver - first page", () => {
  const storiesPaginationResolver = new StoriesPaginationResolver({
    originURL: "https://kriper.net",
  });

  const firstPageUrl = storiesPaginationResolver.resolve(1);

  assertEquals(firstPageUrl, "https://kriper.net/creepystory/");
});

Deno.test("Test StoriesPaginationResolver - second page", () => {
  const storiesPaginationResolver = new StoriesPaginationResolver({
    originURL: "https://kriper.net",
  });

  const firstPageUrl = storiesPaginationResolver.resolve(2);

  assertEquals(firstPageUrl, "https://kriper.net/creepystory/page/2/");
});

Deno.test(
  "Test StoriesPaginationResolver - throw if provided page less than 1",
  () => {
    const storiesPaginationResolver = new StoriesPaginationResolver({
      originURL: "https://kriper.net",
    });

    assertThrows(() => {
      storiesPaginationResolver.resolve(0);
    });
  }
);
