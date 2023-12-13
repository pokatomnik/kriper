import { NY2024StoriesResolver } from "./NY2024StoriesResolver.ts";
import { assertEquals } from "testing";

Deno.test("Test NY2024StoriesResolver", () => {
  const resolver = new NY2024StoriesResolver({
    originURL: "https://kriper.net",
  });
  const ny2024StoriesURL = resolver.resolve();

  assertEquals(
    ny2024StoriesURL,
    "https://kriper.net/12461-%D0%BF%D0%BE%D0%B4%D0%B1%D0%BE%D1%80%D0%BA%D0%B0.html"
  );
});
