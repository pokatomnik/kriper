import { isEmpty } from "services/top-client/IterableUtils.ts";
import { ValidationResult } from "services/top-client/ValidationResult.ts";

/**
 * Checks if list of arrays has no empty array in the middle or at the end
 * of the list
 * @param arrays arrays of items
 * @returns
 */
export const isValid = (
  arrays: ReadonlyArray<Iterable<unknown>>
): ValidationResult => {
  const allEmpty = arrays.every(isEmpty);
  if (allEmpty || arrays.length === 0) {
    return ValidationResult.EMPTY;
  }
  for (let i = arrays.length - 1; i >= 0; --i) {
    const currentArray = arrays[i] ?? [];
    const previousArray = arrays[i - 1];

    if (!previousArray) {
      return ValidationResult.VALID;
    }

    if (isEmpty(currentArray) && !isEmpty(previousArray)) {
      return ValidationResult.INVALID;
    }
  }

  return ValidationResult.VALID;
};
