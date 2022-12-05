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

  public async start() {
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

    const index: IIndex = {
      pageMeta,
      tagsMap: tagsMap,
      dateCreatedISO: new Date().toISOString(),
    };

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
