export interface ITop {
  /**
   * Last week top story id list
   */
  readonly weekTop: ReadonlyArray<string>;

  /**
   * Last month top story id list
   */
  readonly monthTop: ReadonlyArray<string>;

  /**
   * Last year top story id list
   */
  readonly yearTop: ReadonlyArray<string>;

  /**
   * Top story ids of all the time
   */
  readonly allTheTimeTop: ReadonlyArray<string>;
}
