import * as Testing from "testing";

export class FileReader {
  public constructor(protected readonly meta: ImportMeta) {}

  protected getUrlByRelativePath(relativePath: string): URL {
    return new URL(this.meta.resolve(relativePath));
  }

  public async getFileContents(fileName: string): Promise<string> {
    const expectedContentsURL = this.getUrlByRelativePath(fileName);
    const expectedContents = await Deno.readTextFile(expectedContentsURL);
    return expectedContents;
  }
}

export class Snapshot extends FileReader {
  private async exists(url: URL): Promise<boolean> {
    try {
      await Deno.stat(url);
      return true;
    } catch (error) {
      if (error instanceof Deno.errors.NotFound) {
        return false;
      } else {
        throw error;
      }
    }
  }

  protected override getUrlByRelativePath(relativePath: string): URL {
    return super.getUrlByRelativePath(relativePath);
  }

  public async snapshotCheck(
    contentToCheck: string,
    relativeFilePath: string
  ): Promise<void> {
    const expectedContentsURL = this.getUrlByRelativePath(relativeFilePath);
    const expectedContentsURLExists = await this.exists(expectedContentsURL);
    if (expectedContentsURLExists) {
      const expectedContents = await this.getFileContents(relativeFilePath);
      Testing.assertEquals(contentToCheck, expectedContents);
    } else {
      await Deno.writeTextFile(expectedContentsURL, contentToCheck);
      console.log(`Snapshot created: ${expectedContentsURL.toString()}`);
    }
  }
}
