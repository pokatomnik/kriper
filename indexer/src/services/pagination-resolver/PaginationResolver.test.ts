import { PaginationResolver } from "services/pagination-resolver/PaginationResolver.ts";
import { assertEquals, assertThrows } from "testing";

Deno.test("Test PaginationResolver - first page", () => {
  const storiesPaginationResolver = new PaginationResolver({
    originURL: "https://kriper.net",
    shorterAPIURL: "https://yndxai.deno.dev",
  });

  const firstPageUrl = storiesPaginationResolver.resolve(1);

  assertEquals(firstPageUrl, "https://kriper.net/creepystory/");
});

Deno.test("Test PaginationResolver - second page", () => {
  const storiesPaginationResolver = new PaginationResolver({
    originURL: "https://kriper.net",
    shorterAPIURL: "https://yndxai.deno.dev",
  });

  const firstPageUrl = storiesPaginationResolver.resolve(2);

  assertEquals(firstPageUrl, "https://kriper.net/creepystory/page/2/");
});

Deno.test(
  "Test PaginationResolver - throw if provided page less than 1",
  () => {
    const storiesPaginationResolver = new PaginationResolver({
      originURL: "https://kriper.net",
      shorterAPIURL: "https://yndxai.deno.dev",
    });

    assertThrows(() => {
      storiesPaginationResolver.resolve(0);
    });
  }
);
