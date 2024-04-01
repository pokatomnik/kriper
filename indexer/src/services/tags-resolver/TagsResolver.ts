import type { IURLResolver } from "services/lib/IURLResolver.ts";
import type { IURLConfiguration } from "services/configuration/IURLConfiguration.ts";
import { URLConfiguration } from "services/configuration/URLConfiguration.ts";
import { Provide } from "microdi";

@Provide(URLConfiguration)
export class TagsResolver implements IURLResolver<[]> {
  public constructor(private readonly urlConfiguration: IURLConfiguration) {}

  public resolve() {
    return `${this.urlConfiguration.originURL}/tags.html`;
  }
}
