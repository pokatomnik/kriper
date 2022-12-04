import type { IAsyncStorage } from "../lib/IAsyncStorage.ts";
import type { IPathConfiguration } from "../configuration/IPathConfiguration.ts";
import type { IIndex } from "../../domain/IIndex.ts";

export class IndexSaver implements IAsyncStorage<string, IIndex> {
  public constructor(private readonly pathConfiguration: IPathConfiguration) {}

  private getFilePath(indexJSONFileName: string): string {
    return `${this.pathConfiguration.outputPath}/${indexJSONFileName}.json`;
  }

  public async set(name: string, indexJSON: IIndex): Promise<void> {
    const filePath = this.getFilePath(name);
    try {
      await Deno.writeTextFile(filePath, JSON.stringify(indexJSON));
    } catch {
      throw new Error("Failed to save index data");
    }
  }

  public async get(name: string): Promise<IIndex> {
    const filePath = this.getFilePath(name);
    const jsonAsText = await Deno.readTextFile(filePath);
    return JSON.parse(jsonAsText);
  }
}
