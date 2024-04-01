import { isEmpty, len } from "services/top-client/IterableUtils.ts";
import * as Testing from "testing";

Deno.test("EmptyChecker - empty Array", () => {
  const result = isEmpty([]);
  Testing.assertEquals(result, true);
});

Deno.test("EmptyChecker - non-empty Array", () => {
  const result = isEmpty([1]);
  Testing.assertEquals(result, false);
});

Deno.test("EmptyChecker - empty Set", () => {
  const result = isEmpty(new Set([]));
  Testing.assertEquals(result, true);
});

Deno.test("EmptyChecker - non-empty Set", () => {
  const result = isEmpty(new Set([1]));
  Testing.assertEquals(result, false);
});

Deno.test("Length - zero", () => {
  const result = len([]);
  Testing.assertEquals(result, 0);
});

Deno.test("Length - non-zero", () => {
  const result = len([1, 2, 3]);
  Testing.assertEquals(result, 3);
});
