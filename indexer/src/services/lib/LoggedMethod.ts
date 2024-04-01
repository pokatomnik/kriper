import type { ClassMethod } from "decorate";
import { resolve } from "microdi";
import { ILogger } from "services/lib/ILogger.ts";
import { ConsoleLogger } from "services/logger/ConsoleLogger.ts";

export function LoggedAsync<
  TThis extends object,
  TArgs extends ReadonlyArray<unknown>,
  TReturn extends unknown
>(
  logParams: Readonly<{
    start?: (self: TThis, args: TArgs) => string;
    success?: (self: TThis, args: TArgs, result: TReturn) => string;
    failed?: (self: TThis, args: TArgs, error: Error) => string;
  }>,
  unknownError: (e: unknown) => Error = () => new Error("Unknown error"),
  logger: ILogger = resolve(ConsoleLogger)
) {
  return function (
    target: ClassMethod<TThis, Promise<TReturn>, TArgs>,
    _context: ClassMethodDecoratorContext<
      TThis,
      ClassMethod<TThis, Promise<TReturn>, TArgs>
    >
  ) {
    return async function (this: TThis, ...args: TArgs): Promise<TReturn> {
      if (logParams.start) {
        logger.info(logParams.start(this, args));
      }
      try {
        const result = await target.call(this, ...args);
        if (logParams.success) {
          logger.info(logParams.success(this, args, result));
        }
        return result;
      } catch (e) {
        const error = e instanceof Error ? e : unknownError(e);
        if (logParams.failed) {
          logger.info(logParams.failed(this, args, error));
        }
        throw e;
      }
    };
  };
}
