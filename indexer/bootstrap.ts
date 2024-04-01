import { App } from "./src/app.ts";
import { ConsoleLogger } from "services/logger/ConsoleLogger.ts";
import { resolve } from "microdi";

if (import.meta.main) {
  const app = resolve(App);
  app.start();
} else {
  resolve(ConsoleLogger).error(
    "This module must not be imported as module. Run It instead."
  );
}
