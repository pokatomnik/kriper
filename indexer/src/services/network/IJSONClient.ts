export interface IJSONClient {
  post<TBody extends object, TRespose extends object>(
    url: string,
    data: TBody
  ): Promise<TRespose>;
}
