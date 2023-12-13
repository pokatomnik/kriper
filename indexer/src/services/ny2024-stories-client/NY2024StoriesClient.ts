import { provide } from "provide";
import type { IClient } from "../lib/IClient.ts";
import type { IParser } from "../lib/IParser.ts";
import { IURLResolver } from "../lib/IURLResolver.ts";
import type { IHTMLClient } from "../network/IHTMLClient.ts";
import { RetrierHTMLClient } from "../network/RetrierHTMLClient.ts";
import { NY2024StoriesParser } from "../ny2024-stories-parser/NY2024StoriesParser.ts";
import { NY2024StoriesResolver } from "../ny2024-stories-resolver/NY2024StoriesResolver.ts";
import { StoryIdentifierParser } from "../pagemeta-parser/story-identifier-parser/StoryIdentifierParser.ts";

export class NY2024StoriesClient implements IClient<ReadonlyArray<string>, []> {
  public constructor(
    private readonly ny2024StoriesResolver: IURLResolver<[]>,
    private readonly httpClient: IHTMLClient,
    private readonly ny2024StoriesParser: IParser<ReadonlyArray<string>>,
    private readonly storyIdParser: IParser<string>
  ) {}

  public async get(): Promise<ReadonlyArray<string>> {
    const ny2024StoriesURL = this.ny2024StoriesResolver.resolve();
    const content = await this.httpClient.get(ny2024StoriesURL);
    const ny2024StoriesURLs = await this.ny2024StoriesParser.parse(content);

    const ny2024StoryIds = new Set<string>();

    for (const storyURL of ny2024StoriesURLs) {
      const content = await this.httpClient.get(storyURL);
      const storyId = await this.storyIdParser.parse(content);
      ny2024StoryIds.add(storyId);
    }

    return Array.from(ny2024StoryIds);
  }
}

provide(NY2024StoriesClient, [
  NY2024StoriesResolver,
  RetrierHTMLClient,
  NY2024StoriesParser,
  StoryIdentifierParser,
]);
