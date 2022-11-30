export interface IURLResolver<A extends Array<unknown>> {
  resolve(...args: A): string;
}
