import type { IClient } from "../lib/IClient.ts";
import type { IPagination } from "../lib/IPagination.ts";
import type { IParser } from "../lib/IParser.ts";
import type { IURLResolver } from "../lib/IURLResolver.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import { provide } from "provide";
import { PaginationResolver } from "../pagination-resolver/PaginationResolver.ts";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { PaginationParser } from "../pagination-parser/PaginationParser.ts";

export class PagnationClient implements IClient<IPagination, []> {
  public constructor(
    private readonly resolver: IURLResolver<[number]>,
    private readonly httpClient: IHTMLClient,
    private readonly parser: IParser<IPagination>
  ) {}

  public async get(): Promise<IPagination> {
    const paginationURL = this.resolver.resolve(1);
    const content = await this.httpClient.get(paginationURL);
    const pagination = await this.parser.parse(content);

    return Promise.resolve(pagination);
  }
}

provide(PagnationClient, [
  PaginationResolver,
  RetrierHTMLClient,
  PaginationParser,
]);
