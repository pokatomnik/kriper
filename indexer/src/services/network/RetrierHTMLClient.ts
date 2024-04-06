import type { IHTMLClient } from "services/network/IHTMLClient.ts";
import { HTMLClient } from "services/network/HTMLClient.ts";
import { Provide, resolve } from "microdi";
import { BoundMethod, Retry } from "decorate";
import { NetworkConfiguration } from "services/configuration/NetworkConfiguration.ts";

@Provide(HTMLClient)
export class RetrierHTMLClient implements IHTMLClient {
  public constructor(private readonly sourceHTMLClient: IHTMLClient) {}

  @Retry(resolve(NetworkConfiguration).maxNetworkAttempts)
  @BoundMethod
  public async get(url: string): Promise<string> {
    return await this.sourceHTMLClient.get(url);
  }
}
