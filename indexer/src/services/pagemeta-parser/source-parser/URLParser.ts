function isUrl(input: string): boolean {
  try {
    new URL(input);
    return true;
  } catch (_) {
    return false;
  }
}

export function getURL(href: string): string | null {
  if (href.startsWith("javascript:")) {
    return null;
  }
  const looksLikeURL = isUrl(
    href.startsWith("http") ? href : `https://${href}`
  );
  if (looksLikeURL) {
    return href.startsWith("http") ? href : `http://${href}`;
  }
  return null;
}
