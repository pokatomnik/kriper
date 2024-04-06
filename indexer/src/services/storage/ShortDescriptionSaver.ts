import type { IAsyncStorage } from "services/lib/IAsyncStorage.ts";
import type { IPathConfiguration } from "services/configuration/IPathConfiguration.ts";
import { Provide } from "microdi";
import { PathConfiguration } from "services/configuration/PathConfiguration.ts";
import { BoundMethod } from "decorate";

@Provide(PathConfiguration)
export class ShortDescriptionSaver implements IAsyncStorage<string, string> {
  public constructor(private readonly pathConfiguration: IPathConfiguration) {}

  private getFilePath(storyId: string): string {
    return `${this.pathConfiguration.outputPath}/${storyId}.short.md`;
  }

  @BoundMethod
  public async set(storyId: string, shortContent: string): Promise<void> {
    const filePath = this.getFilePath(storyId);
    try {
      await Deno.writeTextFile(filePath, shortContent);
    } catch {
      throw new Error(`Failed to save to ${filePath}`);
    }
  }

  @BoundMethod
  public async get(storyId: string): Promise<string> {
    const filePath = this.getFilePath(storyId);
    try {
      return await Deno.readTextFile(filePath);
    } catch {
      throw new Error(`Failed reading ${filePath}`);
    }
  }
}
