import { IPageMeta } from "./IPageMeta.ts";

export interface ITag {
  readonly tagName: string;
  readonly pages: { [pageTitle: string]: IPageMeta };
}
