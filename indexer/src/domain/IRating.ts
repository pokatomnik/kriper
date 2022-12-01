/**
 * Users rating. Please do not use Its fields for computations.
 */
export interface IRating {
  /**
   * Users marked this story as bad
   */
  readonly bad: number;

  /**
   * Users marked this story as good
   */
  readonly good: number;

  /**
   * Total score
   */
  readonly total: number;
}
