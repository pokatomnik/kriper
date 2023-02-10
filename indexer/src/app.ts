import type { ITagsGroupMap } from "./domain/ITagGroupsMap.ts";
import type { IClient } from "./services/lib/IClient.ts";
import type { IPagination } from "./services/lib/IPagination.ts";
import type { IFetchPageParams } from "./services/lib/IFetchPageParams.ts";
import type { IPageMeta } from "./domain/IPageMeta.ts";
import type { IIndex } from "./domain/IIndex.ts";
import type { IAsyncStorage } from "./services/lib/IAsyncStorage.ts";
import type { ILogger } from "./services/lib/ILogger.ts";
import { provide } from "provide";
import { TagsClient } from "./services/tags-client/TagsClient.ts";
import { PagnationClient } from "./services/pagination-client/PaginationClient.ts";
import { PageListClient } from "./services/pages-list-client/PagesListClient.ts";
import { PageMetaClient } from "./services/pagemeta-client/PageMetaClient.ts";
import { IndexSaver } from "./services/storage/IndexSaver.ts";
import { ConsoleLogger } from "./services/logger/ConsoleLogger.ts";

export class App {
  public constructor(
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

  private makeNumeratedTitle(title: string, number: number) {
    return `${title} (${number})`;
  }

  public async start() {
    const tagsGroupMap = await this.getTagsGroupMap();
    const pagination = await this.getPagination();
    const pagesToFetch = await this.getPagesToFetch(pagination);
    const pageMeta = await this.getPageMeta(pagesToFetch);

    const pageMetaIndex: { [pageTitle: string]: IPageMeta } = {};
    for (let pageMetaItem of pageMeta) {
      if (pageMetaIndex[pageMetaItem.title] === undefined) {
        // Current page title is not a duplicate
        pageMetaIndex[pageMetaItem.title] = pageMetaItem;
      } else {
        // Otherwise, rename this story to "Story Name (N)" to make the name unique
        let i = 1;
        while (
          pageMetaIndex[this.makeNumeratedTitle(pageMetaItem.title, i)] !==
          undefined
        ) {
          ++i;
        }

        const numeratedTitle = this.makeNumeratedTitle(pageMetaItem.title, i);

        pageMetaItem = {
          ...pageMetaItem,
          title: numeratedTitle,
        };

        pageMetaIndex[numeratedTitle] = pageMetaItem;
      }

      for (const currentTagTitle of pageMetaItem.tags) {
        for (const [_, tagGroup] of Object.entries(tagsGroupMap)) {
          for (const [tagTitle, tag] of Object.entries(tagGroup)) {
            if (currentTagTitle === tagTitle) {
              tag.pages.push(pageMetaItem.title);
            }
          }
        }
      }
    }

    const index: IIndex = {
      tagsMap: tagsGroupMap,
      pageMeta: pageMetaIndex,
      dateCreatedISO: new Date().toISOString(),
    };

    await this.saveIndex(index);

    this.logger.info("Process completed successfully");
  }
}

provide(App, [
  TagsClient,
  PagnationClient,
  PageListClient,
  PageMetaClient,
  IndexSaver,
  ConsoleLogger,
]);
