import type { IPageMeta } from "../../domain/IPageMeta.ts";
import type { IParser } from "../lib/IParser.ts";
import type { IHasher } from "../lib/IHasher.ts";
import type { IAsyncStorage } from "../lib/IAsyncStorage.ts";
import type { IUncheckedDate } from "../../domain/IUncheckedDate.ts";

export class PageMetaParser implements IParser<IPageMeta> {
  public constructor(
    private readonly namer: IHasher,
    private readonly titleParser: IParser<string>,
    private readonly contentParser: IParser<string>,
    private readonly authorNicknameParser: IParser<string>,
    private readonly dateCreatedParser: IParser<IUncheckedDate>,
    private readonly numberOfViewsParser: IParser<number>,
    private readonly readingTimeMinutesParser: IParser<number>,
    private readonly sourceParser: IParser<string | null>,
    private readonly ratingParser: IParser<number>,
    private readonly tagsParser: IParser<ReadonlyArray<string>>,
    private readonly seeAlsoParser: IParser<ReadonlyArray<string>>,
    private readonly asyncStorage: IAsyncStorage<string, string>
  ) {}

  public async parse(rawHTML: string): Promise<IPageMeta> {
    const [
      title,
      content,
      authorNickname,
      dateCreated,
      numberOfViews,
      readingTimeMinutes,
      source,
      rating,
      tags,
      seeAlso,
    ] = await Promise.all([
      this.titleParser.parse(rawHTML),
      this.contentParser.parse(rawHTML),
      this.authorNicknameParser.parse(rawHTML),
      this.dateCreatedParser.parse(rawHTML),
      this.numberOfViewsParser.parse(rawHTML),
      this.readingTimeMinutesParser.parse(rawHTML),
      this.sourceParser.parse(rawHTML),
      this.ratingParser.parse(rawHTML),
      this.tagsParser.parse(rawHTML),
      this.seeAlsoParser.parse(rawHTML),
    ]);

    const contentId = this.namer.compute(title);

    await this.asyncStorage.set(contentId, content);

    return Promise.resolve({
      contentId,
      title,
      authorNickname,
      dateCreated,
      numberOfViews,
      readingTimeMinutes,
      source: source ?? undefined,
      rating: rating ?? undefined,
      tags: tags,
      seeAlso: seeAlso,
    });
  }
}

throw new Error("describe provide here");
