import { IPageMeta } from "./IPageMeta.ts";
import type { ITagsGroupMap } from "./ITagGroupsMap.ts";

export interface IIndex {
  readonly pageMeta: { [pageTitle: string]: IPageMeta };
  readonly tagsMap: ITagsGroupMap;
  readonly dateCreatedISO: string;
}
