import type { IUncheckedDate } from "./IUncheckedDate.ts";

export interface IPageMeta {
  /**
   * Unique identifier of the story
   */
  readonly storyId: string;

  /**
   * The title of a page
   */
  readonly title: string;

  /**
   * The real name of story author. Optional, but presented in some stories.
   */
  readonly authorRealName?: string;

  /**
   * Nickname of the author at kriper.net
   */
  readonly authorNickname: string;

  /**
   * Creation date, just date with year, month and day
   */
  readonly dateCreated: IUncheckedDate;

  /**
   * How many times has the page beed viewed
   */
  readonly numberOfViews: number;

  /**
   * Approximate reading times in minutes
   */
  readonly readingTimeMinutes: number;

  /**
   * Optional source of the story.
   * Shows where this story came from.
   * Actually, this field describes a website, i.e. https://mrakopedia.net, https://4stor.ru, etc
   */
  readonly source?: string;

  /**
   * Where is the web version of the story located.
   */
  readonly webpageURL: string;

  /**
   * Rating of the page. Zero if no rating provided.
   */
  readonly rating: number;

  /**
   * Tags of story
   */
  readonly tags: ReadonlyArray<string>;

  /**
   * List of stories similar to this. Contains names.
   */
  readonly seeAlso: ReadonlyArray<string>;

  /**
   * Images on the page, urls
   */
  readonly images: ReadonlyArray<string>;

  /**
   * Videos on the page, yourube urls
   */
  readonly videos: ReadonlyArray<string>;
}
