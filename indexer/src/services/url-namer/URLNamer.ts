import type { IHasher } from "../lib/IHasher.ts";
import { provide } from "provide";
import { NameHasher } from "../name-hasher/NameHasher.ts";

export class URLNamer implements IHasher {
  public constructor(private readonly nameHasher: IHasher) {}

  public compute(url: string): string {
    const uniqueName = new URL(url).pathname
      .split("/")
      .filter(Boolean)[1]
      ?.split(".")[0];
    if (!uniqueName) {
      throw new Error("Failed to parse name");
    }
    return this.nameHasher.compute(uniqueName);
  }
}

provide(URLNamer, [NameHasher]);
