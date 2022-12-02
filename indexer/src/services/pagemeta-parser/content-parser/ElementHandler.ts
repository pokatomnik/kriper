import * as DenoDOM from "denodom";

export type ElementHander = (
  element: DenoDOM.Element,
  container: DenoDOM.Element,
  document: DenoDOM.Document
) => void;
