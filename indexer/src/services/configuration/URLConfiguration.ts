import type { IURLConfiguration } from "./IURLConfiguration.ts";
import { provide } from "provide";

export class URLConfiguration implements IURLConfiguration {
  public readonly originURL = "https://kriper.net";
}

provide(URLConfiguration, []);
