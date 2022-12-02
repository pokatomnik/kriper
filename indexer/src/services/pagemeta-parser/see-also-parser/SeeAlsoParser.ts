import type { IParser } from "../../lib/IParser.ts";
import { provide } from "provide";

/**
 * Mock implementation
 * TODO wait for template changes
 */
export class SeeAlsoParser implements IParser<Array<string>> {
  public parse(_: string): Promise<Array<string>> {
    return Promise.resolve([]);
  }
}

provide(SeeAlsoParser, []);
