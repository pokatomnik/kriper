import type { IURLConfiguration } from "services/configuration/IURLConfiguration.ts";
import type { IURLResolver } from "services/lib/IURLResolver.ts";
import { URLConfiguration } from "services/configuration/URLConfiguration.ts";
import { Provide } from "microdi";

@Provide(URLConfiguration)
export class PaginationResolver implements IURLResolver<[number]> {
  public constructor(private readonly urlConfiguration: IURLConfiguration) {}

  public resolve(pageNumber: number): string {
    if (pageNumber < 1) {
      throw new Error("Min page is 1");
    }

    const origin = this.urlConfiguration.originURL;

    if (pageNumber === 1) {
      return `${origin}/creepystory/`;
    }

    return `${origin}/creepystory/page/${pageNumber}/`;
  }
}
