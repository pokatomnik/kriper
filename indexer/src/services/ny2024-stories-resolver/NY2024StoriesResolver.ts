import type { IURLConfiguration } from "../configuration/IURLConfiguration.ts";
import type { IURLResolver } from "../lib/IURLResolver.ts";
import { URLConfiguration } from "../configuration/URLConfiguration.ts";
import { provide } from "provide";

export class NY2024StoriesResolver implements IURLResolver<[]> {
  public constructor(private readonly urlConfiguration: IURLConfiguration) {}

  public resolve(): string {
    const origin = this.urlConfiguration.originURL;

    return `${origin}/${encodeURIComponent("12461-подборка.html")}`;
  }
}

provide(NY2024StoriesResolver, [URLConfiguration]);
