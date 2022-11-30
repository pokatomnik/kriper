export interface IParser<T> {
  parse(source: string): Promise<T>;
}
