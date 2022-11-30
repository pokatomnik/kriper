export interface IClient<R, A extends Array<unknown>> {
  get(...args: A): Promise<R>;
}
