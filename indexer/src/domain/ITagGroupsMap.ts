import type { ITag } from "domain/ITag.ts";

export interface ITagsGroupMap {
  [groupName: string]: { [tagTitle: string]: ITag };
}
