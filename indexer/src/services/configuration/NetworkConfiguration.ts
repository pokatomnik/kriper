import type { INetworkConfiguration } from "./INetworkConfiguration.ts";
import { provide } from "provide";

export class NetworkConfiguration implements INetworkConfiguration {
  public readonly maxNetworkAttempts = 10;
}

provide(NetworkConfiguration, []);
