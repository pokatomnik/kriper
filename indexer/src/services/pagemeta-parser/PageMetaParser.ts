import type { IPageMeta } from "../../domain/IPageMeta.ts";
import type { IParser } from "../lib/IParser.ts";
import type { IHasher } from "../lib/IHasher.ts";
import type { IAsyncStorage } from "../lib/IAsyncStorage.ts";
import type { IUncheckedDate } from "../../domain/IUncheckedDate.ts";
import { provide } from "provide";
import { URLNamer } from "../url-namer/URLNamer.ts";
import { TitleParser } from "./title-parser/TitleParser.ts";
import { ContentParser } from "./content-parser/ContentParser.ts";
import { AuthorNicknameParser } from "./author-nickname-parser/AuthorNicknameParser.ts";
import { AuthorRealNameParser } from "./author-realname-parser/AuthorRealNameParser.ts";
import { DateCreatedParser } from "./date-created-parser/DateCreatedParser.ts";
import { NumberOfViewsParser } from "./number-of-views-parser/NumberOfViewsParser.ts";
import { ReadingTimeMinutesParser } from "./reading-time-minutes-parser/ReadingTimeMinutesParser.ts";
import { SourceParser } from "./source-parser/SourceParser.ts";
import { WebPageURLParser } from "./webpage-url-parser/WebPageURLParser.ts";
import { RatingParser } from "./rating-parser/RatingParser.ts";
import { TagsParser } from "./tags-parser/TagsParser.ts";
import { SeeAlsoParser } from "./see-also-parser/SeeAlsoParser.ts";
import { ImagesParser } from "./images-parser/ImagesParser.ts";
import { VideosParser } from "./videos-parser/VideosParser.ts";
import { ContentSaver } from "../storage/ContentSaver.ts";

export class PageMetaParser implements IParser<IPageMeta> {
  public constructor(
    private readonly namer: IHasher,
    private readonly titleParser: IParser<string>,
    private readonly contentParser: IParser<string>,
    private readonly authorNicknameParser: IParser<string>,
    private readonly authorRealNameParser: IParser<string | null>,
    private readonly dateCreatedParser: IParser<IUncheckedDate>,
    private readonly numberOfViewsParser: IParser<number>,
    private readonly readingTimeMinutesParser: IParser<number>,
    private readonly sourceParser: IParser<string | null>,
    private readonly webpageURLParser: IParser<string>,
    private readonly ratingParser: IParser<number>,
    private readonly tagsParser: IParser<ReadonlyArray<string>>,
    private readonly seeAlsoParser: IParser<ReadonlyArray<string>>,
    private readonly imagesParser: IParser<ReadonlyArray<string>>,
    private readonly videosParser: IParser<ReadonlyArray<string>>,
    private readonly asyncStorage: IAsyncStorage<string, string>
  ) {}

  public async parse(rawHTML: string): Promise<IPageMeta> {
    const [
      title,
      content,
      authorNickname,
      authorRealName,
      dateCreated,
      numberOfViews,
      readingTimeMinutes,
      source,
      webpageURL,
      rating,
      tags,
      seeAlso,
      images,
      videos,
    ] = await Promise.all([
      this.titleParser.parse(rawHTML),
      this.contentParser.parse(rawHTML),
      this.authorNicknameParser.parse(rawHTML),
      this.authorRealNameParser.parse(rawHTML),
      this.dateCreatedParser.parse(rawHTML),
      this.numberOfViewsParser.parse(rawHTML),
      this.readingTimeMinutesParser.parse(rawHTML),
      this.sourceParser.parse(rawHTML),
      this.webpageURLParser.parse(rawHTML),
      this.ratingParser.parse(rawHTML),
      this.tagsParser.parse(rawHTML),
      this.seeAlsoParser.parse(rawHTML),
      this.imagesParser.parse(rawHTML),
      this.videosParser.parse(rawHTML),
    ]);

    const contentId = this.namer.compute(webpageURL);

    await this.asyncStorage.set(contentId, content);

    return Promise.resolve({
      contentId,
      title,
      authorNickname,
      authorRealName: authorRealName ?? undefined,
      dateCreated,
      numberOfViews,
      readingTimeMinutes,
      source: source ?? undefined,
      webpageURL,
      rating: rating ?? undefined,
      tags: tags,
      seeAlso: seeAlso,
      images,
      videos,
    });
  }
}

provide(PageMetaParser, [
  URLNamer,
  TitleParser,
  ContentParser,
  AuthorNicknameParser,
  AuthorRealNameParser,
  DateCreatedParser,
  NumberOfViewsParser,
  ReadingTimeMinutesParser,
  SourceParser,
  WebPageURLParser,
  RatingParser,
  TagsParser,
  SeeAlsoParser,
  ImagesParser,
  VideosParser,
  ContentSaver,
]);
