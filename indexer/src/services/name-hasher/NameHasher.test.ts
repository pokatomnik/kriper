import { NameHasher } from "./NameHasher.ts";
import * as Testing from "testing";

Deno.test("Namer simple test", () => {
  const namer = new NameHasher();

  const source = "Hello, world!";
  const name = namer.compute(source);

  Testing.assertEquals(name, "Helloworld1161");
});

Deno.test("Test if two different strings have different names", () => {
  const namer = new NameHasher();

  const source0 = "Hello, world!";
  const source1 = "Hello, worl!!";

  const name0 = namer.compute(source0);
  const name1 = namer.compute(source1);

  Testing.assertNotEquals(name0, name1);
});

Deno.test("Test long input string", () => {
  const namer = new NameHasher();

  const source =
    "Hello, world, this is a test of veeeeeeeery loooooooong input string!!!";
  const name = namer.compute(source);

  Testing.assertEquals(name, "Helloworldthisisates6570");
});
