export interface INetworkConfiguration {
  readonly maxNetworkAttempts: number;

  readonly delayBeforeRequestMilliseconds: number;

  readonly delayAfterRequestMilliseconds: number;
}
