import type { IIndex } from "domain/IIndex.ts";
import type { IPageMeta } from "domain/IPageMeta.ts";
import type { ITagsGroupMap } from "domain/ITagGroupsMap.ts";
import { ITop } from "domain/ITop.ts";
import { Provide } from "microdi";
import { HaltOnThrowAsync } from "services/lib/HaltOnThrow.ts";
import type { IAsyncStorage } from "services/lib/IAsyncStorage.ts";
import type { IClient } from "services/lib/IClient.ts";
import type { IFetchPageParams } from "services/lib/IFetchPageParams.ts";
import type { IPagination } from "services/lib/IPagination.ts";
import { LoggedAsync } from "services/lib/LoggedMethod.ts";
import { PageMetaClient } from "services/pagemeta-client/PageMetaClient.ts";
import { PageListClient } from "services/pages-list-client/PagesListClient.ts";
import { PagnationClient } from "services/pagination-client/PaginationClient.ts";
import { IndexSaver } from "services/storage/IndexSaver.ts";
import { TagsClient } from "services/tags-client/TagsClient.ts";
import { TopClient } from "services/top-client/TopClient.ts";

@Provide(
  TopClient,
  TagsClient,
  PagnationClient,
  PageListClient,
  PageMetaClient,
  IndexSaver
)
export class App {
  public constructor(
    private readonly topClient: IClient<ITop, []>,
    private readonly tagsClient: IClient<ITagsGroupMap, []>,
    private readonly paginationClient: IClient<IPagination, []>,
    private readonly pageListClient: IClient<
      Array<IFetchPageParams>,
      [IPagination]
    >,
    private readonly pageMetaClient: IClient<
      ReadonlyArray<IPageMeta>,
      [ReadonlyArray<IFetchPageParams>]
    >,
    private readonly indexSaver: IAsyncStorage<string, IIndex>
  ) {}

  @LoggedAsync({
    start: () => "Started fetching tops...",
    success: () => "Finished fetching tops.",
    failed: (_self, _args, e) => e.message,
  })
  @HaltOnThrowAsync()
  private async getTop(): Promise<ITop> {
    return await this.topClient.get();
  }

  @LoggedAsync({
    start: () => "Started fetching tags...",
    success: () => "Finished fetching tags.",
    failed: (_self, _args, e) => e.message,
  })
  @HaltOnThrowAsync()
  private async getTagsGroupMap(): Promise<ITagsGroupMap> {
    return await this.tagsClient.get();
  }

  @LoggedAsync({
    start: () => "Started fetching pagination...",
    success: () => "Finished fetching pagination.",
    failed: (_self, _args, e) => e.message,
  })
  @HaltOnThrowAsync()
  private async getPagination(): Promise<IPagination> {
    return await this.paginationClient.get();
  }

  @LoggedAsync({
    start: () => "Started fetching pages list...",
    success: () => "Finished fetching pages list.",
    failed: (_self, _args, e) => e.message,
  })
  @HaltOnThrowAsync()
  private async getPagesToFetch(
    pagination: IPagination
  ): Promise<Array<IFetchPageParams>> {
    return await this.pageListClient.get(pagination);
  }

  @LoggedAsync({
    start: () => "Started fetching content and page metadata...",
    success: () => "Finished fetching metadata for pages list.",
    failed: (_self, _args, e) => e.message,
  })
  @HaltOnThrowAsync()
  private async getPageMeta(
    pagesToFetch: Array<IFetchPageParams>
  ): Promise<ReadonlyArray<IPageMeta>> {
    return await this.pageMetaClient.get(pagesToFetch);
  }

  @LoggedAsync({
    start: () => "Started dumping index file...",
    success: () => "Finished dumping index file.",
    failed: (_self, _args, e) => e.message,
  })
  @HaltOnThrowAsync()
  private async saveIndex(index: IIndex) {
    await this.indexSaver.set("index", index);
  }

  @LoggedAsync({ success: () => "Process completed successfully" })
  public async start() {
    const top = await this.getTop();
    const tagsGroupMap = await this.getTagsGroupMap();
    const pagination = await this.getPagination();
    const pagesToFetch = await this.getPagesToFetch(pagination);
    const pageMeta = await this.getPageMeta(pagesToFetch);

    const pageMetaIndex: { [pageTitle: string]: IPageMeta } = {};

    for (const pageMetaItem of pageMeta) {
      pageMetaIndex[pageMetaItem.storyId] = pageMetaItem;
    }

    for (const pageMetaItem of pageMeta) {
      for (const currentTagTitle of pageMetaItem.tags) {
        for (const [_, tagGroup] of Object.entries(tagsGroupMap)) {
          for (const [tagTitle, tag] of Object.entries(tagGroup)) {
            if (currentTagTitle === tagTitle) {
              tag.pages.push(pageMetaItem.storyId);
            }
          }
        }
      }
    }

    const index: IIndex = {
      tagsMap: tagsGroupMap,
      pageMeta: pageMetaIndex,
      top: top,
      dateCreatedISO: new Date().toISOString(),
    };

    await this.saveIndex(index);
  }
}
