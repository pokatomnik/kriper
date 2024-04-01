import type { IPathConfiguration } from "services/configuration/IPathConfiguration.ts";
import { Provide } from "microdi";

@Provide()
export class PathConfiguration implements IPathConfiguration {
  public readonly outputPath = "./out";
}
