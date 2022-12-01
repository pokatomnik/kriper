import type { IURLConfiguration } from "../configuration/IURLConfiguration.ts";
import type { IURLResolver } from "../lib/IURLResolver.ts";
import { URLConfiguration } from "../configuration/URLConfiguration.ts";
import { provide } from "provide";

export class StoriesPaginationResolver implements IURLResolver<[number]> {
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

provide(StoriesPaginationResolver, [URLConfiguration]);
