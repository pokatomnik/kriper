import { IPageMeta } from "./IPageMeta.ts";
import type { ITagsGroupMap } from "./ITagGroupsMap.ts";

export interface IIndex {
  readonly pageMeta: ReadonlyArray<IPageMeta>;
  readonly tagsMap: ITagsGroupMap;
  readonly dateCreatedISO: string;
}
