export interface IProtocolResponseOK<TData extends object> {
  readonly data: TData;
  readonly error: null;
}

export interface IProtocolResponseError {
  readonly data: null;
  readonly error: string;
}

export type IProtocolResponse<TData extends object> =
  | IProtocolResponseOK<TData>
  | IProtocolResponseError;
