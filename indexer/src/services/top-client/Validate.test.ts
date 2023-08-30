import * as Testing from "testing";
import { isValid } from "./Validate.ts";
import { ValidationResult } from "./ValidationResult.ts";

Deno.test("Test empty array", () => {
  const result = isValid([]);
  Testing.assertEquals(result, ValidationResult.EMPTY);
});

Deno.test("Test all empty", () => {
  const result = isValid([[], [], [], []]);
  Testing.assertEquals(result, ValidationResult.EMPTY);
});

Deno.test("Test success case with empty 1st", () => {
  const result = isValid([[], [1], [1, 2]]);
  Testing.assertEquals(result, ValidationResult.VALID);
});

Deno.test("Test success case", () => {
  const result = isValid([[0], [1], [1, 2]]);
  Testing.assertEquals(result, ValidationResult.VALID);
});

Deno.test("Test failed case 00", () => {
  const result = isValid([[], [1, 2], [], [3, 4]]);
  Testing.assertEquals(result, ValidationResult.INVALID);
});

Deno.test("Test failed case 01", () => {
  const result = isValid([[1], [], [2, 3], []]);
  Testing.assertEquals(result, ValidationResult.INVALID);
});

Deno.test("Test failed case 02", () => {
  const result = isValid([[], [], [1], [2, 3]]);
  Testing.assertEquals(result, ValidationResult.VALID);
});

Deno.test("Test failed case 03", () => {
  const result = isValid([[1], [2, 3], [], []]);
  Testing.assertEquals(result, ValidationResult.INVALID);
});

Deno.test("Test failed case 04", () => {
  const result = isValid([[], [1], [2, 3], []]);
  Testing.assertEquals(result, ValidationResult.INVALID);
});

Deno.test("Test failed case 05", () => {
  const result = isValid([[1], [], [], [2, 3]]);
  Testing.assertEquals(result, ValidationResult.INVALID);
});
