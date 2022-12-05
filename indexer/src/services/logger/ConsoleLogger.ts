import type { ILogger } from "../lib/ILogger.ts";
import { provide } from "provide";

enum LogType {
  INFO = "INFO",
  WARN = "WARN",
  ERROR = "ERROR",
}

export class ConsoleLogger implements ILogger {
  private formatMessage(message: string, logType: LogType): string {
    return `[${new Date().toISOString()}][${logType}]${message}`;
  }

  public info(message: string): void {
    const formatted = this.formatMessage(message, LogType.INFO);
    console.info(formatted);
  }

  public warn(message: string): void {
    const formatted = this.formatMessage(message, LogType.WARN);
    console.log(formatted);
  }

  public error(message: string): void {
    const formatted = this.formatMessage(message, LogType.ERROR);
    console.log(formatted, LogType.ERROR);
  }
}

provide(ConsoleLogger, []);
