import { ValidationResult } from "services/top-client/ValidationResult.ts";

export interface ShuffleResult<T> {
  readonly result: ReadonlyArray<Iterable<T>>;
  readonly validation: ValidationResult;
}
