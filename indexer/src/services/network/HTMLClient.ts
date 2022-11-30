import type { IHTMLClient } from "./IHTMLClient.ts";
import { provide } from "provide";

export class HTMLClient implements IHTMLClient {
  public async get(url: string): Promise<string> {
    const response = await fetch(url);
    return await response.text();
  }
}

provide(HTMLClient, []);
