import { Provide } from "microdi";
import type { IProtocolResponse } from "domain/IProtocolResponse.ts";
import type { YndxAIResponse } from "domain/YndxAIResponse.ts";
import type { IClient } from "services/lib/IClient.ts";
import type { IJSONClient } from "services/network/IJSONClient.ts";
import { RetrierJSONClient } from "services/network/RetrierJSONClient.ts";
import { BoundMethod } from "decorate";
import { URLConfiguration } from "services/configuration/URLConfiguration.ts";
import { IURLConfiguration } from "services/configuration/IURLConfiguration.ts";
import { ShortDescriptionParser } from "services/short-description-parser/ShortDescriptionParser.ts";
import { IParser } from "services/lib/IParser.ts";

@Provide(URLConfiguration, RetrierJSONClient, ShortDescriptionParser)
export class ShortDescriptionClient
  implements IClient<string | null, [string]>
{
  public constructor(
    private readonly urlConfiguration: IURLConfiguration,
    private readonly jsonClient: IJSONClient,
    private readonly shortDescriptionParser: IParser<string>
  ) {}

  @BoundMethod
  public async get(storyURL: string): Promise<string | null> {
    const shorterAPIURL = this.urlConfiguration.shorterAPIURL;
    try {
      const response = await this.jsonClient.post<
        Readonly<{ url: string }>,
        IProtocolResponse<YndxAIResponse>
      >(shorterAPIURL, { url: storyURL });
      if (response.data?.summary) {
        return await this.shortDescriptionParser.parse(response.data.summary);
      }
      return null;
    } catch {
      return null;
    }
  }
}
