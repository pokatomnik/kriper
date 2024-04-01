import type { INetworkConfiguration } from "services/configuration/INetworkConfiguration.ts";
import { Provide } from "microdi";

@Provide()
export class NetworkConfiguration implements INetworkConfiguration {
  public readonly maxNetworkAttempts = 100;

  public readonly delayBeforeRequestMilliseconds = 0;

  public readonly delayAfterRequestMilliseconds = 0;
}
