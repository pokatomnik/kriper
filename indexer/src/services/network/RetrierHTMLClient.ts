import { IHTMLClient } from "./IHTMLClient.ts";
import { HTMLClient } from "./HTMLClient.ts";
import { provide } from "provide";
import { INetworkConfiguration } from "../configuration/INetworkConfiguration.ts";
import { NetworkConfiguration } from "../configuration/NetworkConfiguration.ts";

export class RetrierHTMLClient implements IHTMLClient {
  public constructor(
    private readonly configuration: INetworkConfiguration,
    private readonly sourceHTMLClient: IHTMLClient
  ) {}

  private readonly defaultError = new Error("Unknown fetch error");

  public async get(url: string): Promise<string> {
    const maxNetworkAttempts = this.configuration.maxNetworkAttempts;
    let lastError = this.defaultError;
    let currentAttempt = 0;
    while (currentAttempt < maxNetworkAttempts)
      try {
        return await this.sourceHTMLClient.get(url);
      } catch (e) {
        const error = e instanceof Error ? e : this.defaultError;
        ++currentAttempt;
        lastError = error;
      }
    throw lastError;
  }
}

provide(RetrierHTMLClient, [NetworkConfiguration, HTMLClient]);
