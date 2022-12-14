import type { IHTMLClient } from "./IHTMLClient.ts";
import type { INetworkConfiguration } from "../configuration/INetworkConfiguration.ts";
import { provide } from "provide";
import { NetworkConfiguration } from "../configuration/NetworkConfiguration.ts";

type ErrorDetector = (response: Response) => Promise<boolean>;

export class HTMLClient implements IHTMLClient {
  private readonly errorDetectors: ReadonlyArray<ErrorDetector> = [
    (response) => Promise.resolve(!response.ok),
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

provide(HTMLClient, [NetworkConfiguration]);
