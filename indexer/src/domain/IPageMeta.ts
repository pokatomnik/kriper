import type { IRating } from "./IRating.ts";
import type { IUncheckedDate } from "./IUncheckedDate.ts";

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
   * Optional source of the page. Can be omitted.
   */
  readonly source?: string;

  /**
   * Rating is optional because some pages does not have It.
   */
  readonly rating?: IRating;

  /**
   * Tags of story
   */
  readonly tags: ReadonlyArray<string>;

  /**
   * List of stories similar to this. Contains names.
   */
  readonly seeAlso: ReadonlyArray<string>;
}
