import type { IAsyncStorage } from "../lib/IAsyncStorage.ts";
import type { IPathConfiguration } from "../configuration/IPathConfiguration.ts";
import { provide } from "provide";
import { PathConfiguration } from "../configuration/PathConfiguration.ts";

export class ContentSaver implements IAsyncStorage<string, string> {
  public constructor(private readonly pathConfiguration: IPathConfiguration) {}

  private getFilePath(storyId: string): string {
    return `${this.pathConfiguration.outputPath}/${storyId}.md`;
  }

  public async set(storyId: string, content: string): Promise<void> {
    const filePath = this.getFilePath(storyId);
    try {
      await Deno.writeTextFile(filePath, content);
    } catch {
      throw new Error(`Failed to save to ${filePath}`);
    }
  }

  public async get(storyId: string): Promise<string> {
    const filePath = this.getFilePath(storyId);
    try {
      return await Deno.readTextFile(filePath);
    } catch {
      throw new Error(`Failed reading ${filePath}`);
    }
  }
}

provide(ContentSaver, [PathConfiguration]);
