import type { IJSONClient } from "services/network/IJSONClient.ts";
import { JSONClient } from "services/network/JSONClient.ts";
import { Provide, resolve } from "microdi";
import { BoundMethod, Retry } from "decorate";
import { NetworkConfiguration } from "services/configuration/NetworkConfiguration.ts";

@Provide(JSONClient)
export class RetrierJSONClient implements IJSONClient {
  public constructor(private readonly sourceJSONClient: IJSONClient) {}

  @Retry<RetrierJSONClient, ReadonlyArray<never>, never>(
    resolve(NetworkConfiguration).maxNetworkAttempts
  )
  @BoundMethod
  public async post<TBody extends object, TRespose extends object>(
    url: string,
    data: TBody
  ): Promise<TRespose> {
    return await this.sourceJSONClient.post<TBody, TRespose>(url, data);
  }
}
