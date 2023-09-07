import { ValidationResult } from "./ValidationResult.ts";

export interface ShuffleResult<T> {
  readonly result: ReadonlyArray<Iterable<T>>;
  readonly validation: ValidationResult;
}
