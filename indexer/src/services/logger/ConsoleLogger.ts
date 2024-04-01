import type { ILogger } from "services/lib/ILogger.ts";
import { Provide } from "microdi";

enum LogType {
  INFO = "INFO",
  WARN = "WARN",
  ERROR = "ERROR",
}

@Provide()
export class ConsoleLogger implements ILogger {
  private formatMessage(message: string, logType: LogType): string {
    return `[${new Date().toISOString()}] [${logType}] ${message}`;
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
