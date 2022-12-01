import type { IRating } from "./IRating.ts";

export interface IPageMeta {
  /**
   * The identifier of the markdown contents placed in a file with name `{contentId}.md`
   */

  readonly contentId: string;

  /**
   * The title of a page
   */
  readonly title: string;

  /**
   * Nickname of the author at kriper.net
   */
  readonly authorNickname: string;

  /**
   * Creation date, ISO format
   */
  readonly dateCreated: string;

  /**
   * How many times has the page beed viewed
   */
  readonly numberOfViews: number;

  /**
   * Approximate reading times in minutes
   */
  readonly readingTimeMinutes: number;

  /**
   * Optional source of the page. Can be omitted.
   */
  readonly source: string;

  /**
   * Rating is optional because some pages does not have It.
   */
  readonly rating?: IRating;

  /**
   * Tags of story
   */
  readonly tags?: ReadonlyArray<string>;

  /**
   * List of stories similar to this. Contains names.
   */
  readonly seeAlso?: ReadonlyArray<string>;
}
