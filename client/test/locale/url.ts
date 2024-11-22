import { TEST_LOCALE } from './constants';

export function withLocalePath(path: string): string {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`;
  return `/${TEST_LOCALE}${normalizedPath}`;
}
