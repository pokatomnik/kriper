import type { ClassMethod } from "decorate";
import { resolve } from "microdi";
import type { ILogger } from "services/lib/ILogger.ts";
import { ConsoleLogger } from "services/logger/ConsoleLogger.ts";

export function HaltOnThrowAsync<
  TThis extends object,
  TArgs extends ReadonlyArray<unknown>,
  TReturn extends unknown
>(
  params?: Readonly<{
    distinctError?: (e: Error) => boolean;
    haltMessage?: (e: Error) => string;
    exitCode?: (e: Error) => number;
    handleNonError?: (e: unknown) => Error;
    logger?: ILogger;
  }>
) {
  const distinctError: (e: Error) => boolean =
    params?.distinctError ?? (() => true);

  const haltMessage: (e: Error) => string =
    params?.haltMessage ?? ((e) => e.message);

  const exitCode: (e: Error) => number = params?.exitCode ?? (() => 1);

  const handleNonError: (e: unknown) => Error =
    params?.handleNonError ??
    ((e) =>
      e instanceof Error ? e : new Error("Unknown critical error. Now halt"));

  const logger: ILogger = resolve(ConsoleLogger);

  return function (
    target: ClassMethod<TThis, Promise<TReturn>, TArgs>,
    _context: ClassMethodDecoratorContext<
      TThis,
      ClassMethod<TThis, Promise<TReturn>, TArgs>
    >
  ) {
    return async function (this: TThis, ...args: TArgs): Promise<TReturn> {
      try {
        return await target.call(this, ...args);
      } catch (e) {
        const error = e instanceof Error ? e : handleNonError(e);
        const isCritical = distinctError(error);
        if (isCritical) {
          logger.error(haltMessage(error));
        }
        Deno.exit(exitCode(error));
      }
    };
  };
}
