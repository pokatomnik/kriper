import { Provide } from "microdi";
import type { IJSONClient } from "services/network/IJSONClient.ts";
import { NetworkConfiguration } from "services/configuration/NetworkConfiguration.ts";
import { INetworkConfiguration } from "services/configuration/INetworkConfiguration.ts";

@Provide(NetworkConfiguration)
export class JSONClient implements IJSONClient {
  public constructor(private readonly configuration: INetworkConfiguration) {}

  private async delay(delayMilliseconds: number): Promise<void> {
    await new Promise((resolve) => setTimeout(resolve, delayMilliseconds));
  }

  public async post<TBody extends object, TRespose extends object>(
    url: string,
    data: TBody
  ): Promise<TRespose> {
    await this.delay(this.configuration.delayBeforeRequestMilliseconds);
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    const json: TRespose = await response.json();
    await this.delay(this.configuration.delayAfterRequestMilliseconds);
    return json;
  }
}
