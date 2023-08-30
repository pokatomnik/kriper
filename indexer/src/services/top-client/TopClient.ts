import type { IClient } from "../lib/IClient.ts";
import type { ITop } from "../../domain/ITop.ts";
import type { IURLResolver } from "../lib/IURLResolver.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import type { IParser } from "../lib/IParser.ts";
import { TopTypeResolver } from "../top-type-resolver/TopTypeResolver.ts";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { TopIdsParser } from "../top-ids-parser/TopIdsParser.ts";
import { provide } from "provide";

type Mutable<Type> = {
  -readonly [Key in keyof Type]: Type[Key];
};

export class TopClient implements IClient<ITop, []> {
  public constructor(
    private readonly topTypeResolver: IURLResolver<[keyof ITop]>,
    private readonly htmlClient: IHTMLClient,
    private readonly topIdsParser: IParser<ReadonlySet<string>>
  ) {}

  public async get(): Promise<ITop> {
    const weekTopURL = this.topTypeResolver.resolve("weekTop");
    const monthTopURL = this.topTypeResolver.resolve("monthTop");
    const yearTopURL = this.topTypeResolver.resolve("yearTop");
    const allTheTimeTopUrl = this.topTypeResolver.resolve("allTheTimeTop");

    const urls: ReadonlyMap<keyof ITop, string> = new Map([
      ["weekTop", weekTopURL],
      ["monthTop", monthTopURL],
      ["yearTop", yearTopURL],
      ["allTheTimeTop", allTheTimeTopUrl],
    ]);

    const result: Mutable<ITop> = {
      weekTop: [],
      monthTop: [],
      yearTop: [],
      allTheTimeTop: [],
    };

    for (const [topId, topIdUrl] of urls.entries()) {
      const topsHTML = await this.htmlClient.get(topIdUrl);
      const topIds = await this.topIdsParser.parse(topsHTML);
      result[topId] = Array.from(topIds);
    }

    return result;
  }
}

provide(TopClient, [TopTypeResolver, RetrierHTMLClient, TopIdsParser]);
