import { IPageMeta } from "./IPageMeta.ts";
import type { ITagsGroupMap } from "./ITagGroupsMap.ts";
import type { ITop } from "./ITop.ts";

export interface IIndex {
  readonly pageMeta: { [storyId: string]: IPageMeta };
  readonly tagsMap: ITagsGroupMap;
  readonly top: ITop;
  readonly ny2024: ReadonlyArray<string>;
  readonly dateCreatedISO: string;
}
