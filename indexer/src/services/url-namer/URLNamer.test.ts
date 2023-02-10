import { URLNamer } from "./URLNamer.ts";
import { NameHasher } from "../name-hasher/NameHasher.ts";
import * as Testing from "testing";

Deno.test("Test correct story URL - correct URL", () => {
  const namer = new URLNamer(new NameHasher());

  const url =
    "https://kriper.net/creepystory/12162-%D0%BF%D0%B0%D0%BB%D1%8C%D1%86%D1%8B.html";
  const name = namer.compute(url);
  Testing.assertEquals(name, "DBFDBDBBDCDDB2177");
});

Deno.test("Test correct story URL - Incorrect URL", () => {
  const namer = new URLNamer(new NameHasher());

  const url = "https://kriper.net/tags.html";
  Testing.assertThrows(() => {
    namer.compute(url);
  });
});
