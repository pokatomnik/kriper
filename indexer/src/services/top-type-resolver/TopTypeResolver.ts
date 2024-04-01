import type { IURLConfiguration } from "services/configuration/IURLConfiguration.ts";
import type { IURLResolver } from "services/lib/IURLResolver.ts";
import type { ITop } from "domain/ITop.ts";
import { URLConfiguration } from "services/configuration/URLConfiguration.ts";
import { Provide } from "microdi";

@Provide(URLConfiguration)
export class TopTypeResolver implements IURLResolver<[keyof ITop]> {
  private static urlMapping: { readonly [key in keyof ITop]: string } = {
    weekTop: "/top/top-week/",
    monthTop: "/top/top-month/",
    yearTop: "/top/top-year/",
    allTheTimeTop: "/top/top-alltime/",
  };

  public constructor(private readonly urlConfiguration: IURLConfiguration) {}

  public resolve(topType: keyof ITop): string {
    const origin = this.urlConfiguration.originURL;
    return `${origin}${TopTypeResolver.urlMapping[topType]}`;
  }
}
