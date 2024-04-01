import type { IPageMeta } from "domain/IPageMeta.ts";
import type { ITagsGroupMap } from "domain/ITagGroupsMap.ts";
import type { ITop } from "domain/ITop.ts";

export interface IIndex {
  readonly pageMeta: { [storyId: string]: IPageMeta };
  readonly tagsMap: ITagsGroupMap;
  readonly top: ITop;
  readonly dateCreatedISO: string;
}
