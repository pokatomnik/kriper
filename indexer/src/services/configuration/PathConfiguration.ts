import type { IPathConfiguration } from "./IPathConfiguration.ts";
import { provide } from "provide";

export class PathConfiguration implements IPathConfiguration {
  public readonly outputPath = "./out";
}

provide(PathConfiguration, []);
