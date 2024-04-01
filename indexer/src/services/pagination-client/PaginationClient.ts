import type { IClient } from "services/lib/IClient.ts";
import type { IPagination } from "services/lib/IPagination.ts";
import type { IParser } from "services/lib/IParser.ts";
import type { IURLResolver } from "services/lib/IURLResolver.ts";
import type { IHTMLClient } from "services/network/IHTMLClient.ts";
import { Provide } from "microdi";
import { PaginationResolver } from "services/pagination-resolver/PaginationResolver.ts";
import { RetrierHTMLClient } from "services/network/RetrierHTMLClient.ts";
import { PaginationParser } from "services/pagination-parser/PaginationParser.ts";

@Provide(PaginationResolver, RetrierHTMLClient, PaginationParser)
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
