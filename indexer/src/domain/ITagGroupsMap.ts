import type { ITag } from "./ITag.ts";

export interface ITagsGroupMap {
  [groupName: string]: { [tagTitle: string]: ITag };
}
