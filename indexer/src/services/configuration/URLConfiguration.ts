import type { IURLConfiguration } from "services/configuration/IURLConfiguration.ts";
import { Provide } from "microdi";

@Provide()
export class URLConfiguration implements IURLConfiguration {
  public readonly originURL = "https://kriper.net";

  public readonly shorterAPIURL = "https://yndxai.deno.dev/api/summarize";
}
