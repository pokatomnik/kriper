import type { IClient } from "../lib/IClient.ts";

export interface IHTMLClient extends IClient<string, [url: string]> {}
