import type { INetworkConfiguration } from "./INetworkConfiguration.ts";
import { provide } from "provide";

export class NetworkConfiguration implements INetworkConfiguration {
  public readonly maxNetworkAttempts = 100;

  public readonly delayBeforeRequestMilliseconds = 0;

  public readonly delayAfterRequestMilliseconds = 0;
}

provide(NetworkConfiguration, []);
