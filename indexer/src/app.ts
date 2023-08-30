import type { ITagsGroupMap } from "./domain/ITagGroupsMap.ts";
import type { IClient } from "./services/lib/IClient.ts";
import type { IPagination } from "./services/lib/IPagination.ts";
import type { IFetchPageParams } from "./services/lib/IFetchPageParams.ts";
import type { IPageMeta } from "./domain/IPageMeta.ts";
import type { IIndex } from "./domain/IIndex.ts";
import type { IAsyncStorage } from "./services/lib/IAsyncStorage.ts";
import type { ILogger } from "./services/lib/ILogger.ts";
import { provide } from "provide";
import { TopClient } from "./services/top-client/TopClient.ts";
import { TagsClient } from "./services/tags-client/TagsClient.ts";
import { PagnationClient } from "./services/pagination-client/PaginationClient.ts";
import { PageListClient } from "./services/pages-list-client/PagesListClient.ts";
import { PageMetaClient } from "./services/pagemeta-client/PageMetaClient.ts";
import { IndexSaver } from "./services/storage/IndexSaver.ts";
import { ConsoleLogger } from "./services/logger/ConsoleLogger.ts";
import { ITop } from "./domain/ITop.ts";

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
    private readonly indexSaver: IAsyncStorage<string, IIndex>,
    private readonly logger: ILogger
  ) {}

  private async getTop(): Promise<ITop> {
    let top: ITop;
    this.logger.info("Startted fetching tops...");
    try {
      top = await this.topClient.get();
      this.logger.info("Finished fetching top.");
    } catch (e) {
      const error =
        e instanceof Error ? e : new Error("Unknown top fethcing error");
      this.logger.error(error.message);
      Deno.exit(1);
    }
    return top;
  }

  private async getTagsGroupMap(): Promise<ITagsGroupMap> {
    let tagsMap: ITagsGroupMap;
    this.logger.info("Started fetching tags...");
    try {
      tagsMap = await this.tagsClient.get();
      this.logger.info("Finished fetching tags.");
    } catch (e) {
      const error =
        e instanceof Error ? e : new Error("Unknown tags fetching error");
      this.logger.error(error.message);
      Deno.exit(1);
    }
    return tagsMap;
  }

  private async getPagination(): Promise<IPagination> {
    let pagination: IPagination;
    this.logger.info("Started fetching pagination...");
    try {
      pagination = await this.paginationClient.get();
      this.logger.info("Finished fetching pagination.");
    } catch (e) {
      const error =
        e instanceof Error ? e : new Error("Unknown pagination fetching error");
      this.logger.error(error.message);
      Deno.exit(1);
    }
    return pagination;
  }

  private async getPagesToFetch(
    pagination: IPagination
  ): Promise<Array<IFetchPageParams>> {
    let pagesToFetch: Array<IFetchPageParams>;
    this.logger.info("Started fetching pages list...");
    try {
      pagesToFetch = await this.pageListClient.get(pagination);
      this.logger.info("Finished fetching pages list.");
    } catch (e) {
      const error =
        e instanceof Error ? e : new Error("Unknown fetching pages list error");
      this.logger.error(error.message);
      Deno.exit(1);
    }
    return pagesToFetch;
  }

  private async getPageMeta(
    pagesToFetch: Array<IFetchPageParams>
  ): Promise<ReadonlyArray<IPageMeta>> {
    let pageMeta: ReadonlyArray<IPageMeta>;
    this.logger.info("Started fetching content and page metadata...");
    try {
      pageMeta = await this.pageMetaClient.get(pagesToFetch);
      this.logger.info("Finished fetching metadata for pages list.");
    } catch (e) {
      const error =
        e instanceof Error ? e : new Error("Unknown fetching content error");
      this.logger.error(error.message);
      Deno.exit(1);
    }
    return pageMeta;
  }

  private async saveIndex(index: IIndex) {
    this.logger.info("Started dumping index file...");
    try {
      await this.indexSaver.set("index", index);
      this.logger.info("Finished dumping index file.");
    } catch (e) {
      const error =
        e instanceof Error ? e : new Error("Failed to dump index file.");
      this.logger.error(error.message);
      Deno.exit(1);
    }
  }

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

    this.logger.info("Process completed successfully");
  }
}

provide(App, [
  TopClient,
  TagsClient,
  PagnationClient,
  PageListClient,
  PageMetaClient,
  IndexSaver,
  ConsoleLogger,
]);
