export interface IAsyncStorage<K, V> {
  /**
   * Sets a value by key
   * @param key storage key
   * @param value value to set
   */
  set(key: K, value: V): Promise<void>;

  /**
   * Gets a value by key. If the key is missing, throw the Error
   * @param key
   * @returns Promise with value
   */
  get(key: K): Promise<V>;
}
