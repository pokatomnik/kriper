import { isValid } from "services/top-client/Validate.ts";
import { ValidationResult } from "services/top-client/ValidationResult.ts";
import { len } from "services/top-client/IterableUtils.ts";
import { type ShuffleResult } from "services/top-client/ShuffleResult.ts";

export const shuffle = <T extends unknown>(
  arrays: ReadonlyArray<Iterable<T>>
): ShuffleResult<T> => {
  const validationResult = isValid(arrays);
  if (validationResult === ValidationResult.INVALID) {
    return { result: arrays, validation: ValidationResult.INVALID };
  }

  if (validationResult === ValidationResult.EMPTY) {
    return {
      result: Array.from(arrays, (arr) => Array.from(arr)),
      validation: ValidationResult.EMPTY,
    };
  }

  const arraysCopy: Array<Iterable<T>> = Array.from(arrays);

  for (let i = arraysCopy.length - 1; i >= 0; --i) {
    const currentArray = arraysCopy[i] ?? [];
    const previousArray = arraysCopy[i - 1];
    if (!previousArray) {
      return { result: arraysCopy, validation: ValidationResult.VALID };
    }

    if (len(previousArray) === 0) {
      arraysCopy[i - 1] = Array.from(currentArray);
    }
  }

  return { result: arraysCopy, validation: ValidationResult.VALID };
};
