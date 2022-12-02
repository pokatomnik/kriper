import type { IAsyncStorage } from "../lib/IAsyncStorage.ts";
import type { IPathConfiguration } from "../configuration/IPathConfiguration.ts";

export class ContentSaver implements IAsyncStorage<string, string> {
  public constructor(private readonly pathConfiguration: IPathConfiguration) {}

  private getFilePath(contentId: string): string {
    return `${this.pathConfiguration.outputPath}/${contentId}.md`;
  }

  public async set(contentId: string, content: string): Promise<void> {
    const filePath = this.getFilePath(contentId);
    try {
      await Deno.writeTextFile(filePath, content);
    } catch {
      throw new Error(`Failed to save to ${filePath}`);
    }
  }

  public async get(contentId: string): Promise<string> {
    const filePath = this.getFilePath(contentId);
    try {
      return await Deno.readTextFile(filePath);
    } catch {
      throw new Error(`Failed reading ${filePath}`);
    }
  }
}
