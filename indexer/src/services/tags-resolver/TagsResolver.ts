import type { IURLResolver } from "../lib/IURLResolver.ts";
import type { IURLConfiguration } from "../configuration/IURLConfiguration.ts";
import { URLConfiguration } from "../configuration/URLConfiguration.ts";
import { provide } from "provide";

export class TagsResolver implements IURLResolver<[]> {
  public constructor(private readonly urlConfiguration: IURLConfiguration) {}

  public resolve() {
    return `${this.urlConfiguration.originURL}/tags.html`;
  }
}

provide(TagsResolver, [URLConfiguration]);
