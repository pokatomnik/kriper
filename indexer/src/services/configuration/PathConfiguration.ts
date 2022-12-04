import type { IPathConfiguration } from "./IPathConfiguration.ts";
import { provide } from "https://deno.land/x/microdi@v0.0.3/Provider.ts";

export class PathConfiguration implements IPathConfiguration {
  public readonly outputPath = "./out";
}

provide(PathConfiguration, []);
