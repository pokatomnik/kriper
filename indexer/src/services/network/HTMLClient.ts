import { Provide } from "microdi";
import { NetworkConfiguration } from "services/configuration/NetworkConfiguration.ts";
import type { INetworkConfiguration } from "services/configuration/INetworkConfiguration.ts";
import type { IHTMLClient } from "services/network/IHTMLClient.ts";

type ErrorDetector = (response: Response) => Promise<boolean>;

@Provide(NetworkConfiguration)
export class HTMLClient implements IHTMLClient {
  private readonly errorDetectors: ReadonlyArray<ErrorDetector> = [
    // Disable, the HTTP code tells nothing about real response quality
    // (response) => Promise.resolve(!response.ok),
    async (response) =>
      (await response.text()).toLocaleLowerCase().includes("mysql"),
  ];

  public constructor(
    private readonly networkConfiguration: INetworkConfiguration
  ) {}

  private async delay(delayMilliseconds: number): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, delayMilliseconds));
  }

  public async get(url: string): Promise<string> {
    await this.delay(this.networkConfiguration.delayBeforeRequestMilliseconds);
    const response = await fetch(url);

    for (const errorDetector of this.errorDetectors) {
      if (await errorDetector(response.clone())) {
        throw new Error("Failed to fetch");
      }
    }

    const textResponse = await response.text();
    await this.delay(this.networkConfiguration.delayAfterRequestMilliseconds);
    return textResponse;
  }
}
