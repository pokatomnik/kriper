import { App } from "./src/app.ts";
import { resolve } from "resolve";

if (import.meta.main) {
  const app = resolve(App);
  app.start();
}
