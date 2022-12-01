import type { IHasher } from "../lib/IHasher.ts";
import { provide } from "provide";

export class NameHasher implements IHasher {
  private static readonly MAX_LENGTH = 20;

  private readonly alphabetRegex = /[a-zA-Zа-яА-я]/;

  public compute(source: string): string {
    if (source.length === 0) {
      throw new Error("Input string is empty");
    }
    let result = "";
    let sum = 0;
    for (let i = 0; i < source.length; ++i) {
      const char = source[i] ?? "";
      if (
        result.length < NameHasher.MAX_LENGTH &&
        this.alphabetRegex.test(char)
      ) {
        result += char;
      }
      sum += char.charCodeAt(0);
    }
    return result.concat(sum.toString());
  }
}

provide(NameHasher, []);
