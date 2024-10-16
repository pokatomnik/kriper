import type { IClient } from "services/lib/IClient.ts";
import type { ITop } from "domain/ITop.ts";
import type { IURLResolver } from "services/lib/IURLResolver.ts";
import type { IHTMLClient } from "services/network/IHTMLClient.ts";
import type { IParser } from "services/lib/IParser.ts";
import { TopTypeResolver } from "services/top-type-resolver/TopTypeResolver.ts";
import { RetrierHTMLClient } from "services/network/RetrierHTMLClient.ts";
import { TopIdsParser } from "services/top-ids-parser/TopIdsParser.ts";
import { Provide } from "microdi";
import { shuffle } from "services/top-client/Shuffle.ts";
import { ValidationResult } from "services/top-client/ValidationResult.ts";

type Mutable<Type> = {
  -readonly [Key in keyof Type]: Type[Key];
};

@Provide(TopTypeResolver, RetrierHTMLClient, TopIdsParser)
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

    const { result: shuffleResult, validation: shuffleValidation } = shuffle([
      result.weekTop,
      result.monthTop,
      result.yearTop,
      result.allTheTimeTop,
    ]);

    if (shuffleValidation === ValidationResult.INVALID) {
      throw new Error("Invalid tops");
    }

    if (shuffleValidation === ValidationResult.EMPTY) {
      throw new Error("Empty tops");
    }

    const [weekTop = [], monthTop = [], yearTop = [], allTheTimeTop = []] =
      shuffleResult;

    result.weekTop = Array.from(weekTop);
    result.monthTop = Array.from(monthTop);
    result.yearTop = Array.from(yearTop);
    result.allTheTimeTop = Array.from(allTheTimeTop);

    return result;
  }
}
