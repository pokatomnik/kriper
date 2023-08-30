export const isEmpty = (value: Iterable<unknown>) => {
  for (const _ of value) {
    return false;
  }
  return true;
};

export const len = (value: Iterable<unknown>) => {
  let i = 0;
  for (const _ of value) ++i;
  return i;
};
