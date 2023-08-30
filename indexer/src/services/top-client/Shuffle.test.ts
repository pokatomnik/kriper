import { shuffle } from "./Shuffle.ts";
import * as Testing from "testing";
import { ValidationResult } from "./ValidationResult.ts";

Deno.test("Test Shuffle - empty list", () => {
  const { result, validation } = shuffle([]);
  Testing.assertEquals(validation, ValidationResult.EMPTY);
  Testing.assertEquals(JSON.stringify(result), JSON.stringify([]));
});

Deno.test("Test Shuffle - empty arrays", () => {
  const { result, validation } = shuffle([[], [], []]);
  Testing.assertEquals(validation, ValidationResult.EMPTY);
  Testing.assertEquals(JSON.stringify(result), JSON.stringify([[], [], []]));
});

Deno.test("Test Shuffle - succes case 00", () => {
  const { result, validation } = shuffle([[], [1], [2, 3]]);
  Testing.assertEquals(validation, ValidationResult.VALID);
  Testing.assertEquals(
    JSON.stringify(result),
    JSON.stringify([[1], [1], [2, 3]])
  );
});

Deno.test("Test Shuffle - succes case 01", () => {
  const { result, validation } = shuffle([[0], [1], [2, 3]]);
  Testing.assertEquals(validation, ValidationResult.VALID);
  Testing.assertEquals(
    JSON.stringify(result),
    JSON.stringify([[0], [1], [2, 3]])
  );
});

Deno.test("Test Shuffle - fail case", () => {
  const { result, validation } = shuffle([[], [1], [], [2, 3]]);
  Testing.assertEquals(validation, ValidationResult.INVALID);
  Testing.assertEquals(
    JSON.stringify(result),
    JSON.stringify([[], [1], [], [2, 3]])
  );
});
