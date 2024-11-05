import { getRequestConfig } from 'next-intl/server';
import { notFound } from 'next/navigation';
import { DEFAULT_LOCALE, isSupportedLocale } from './locale';

export default getRequestConfig(async ({ requestLocale }) => {
  const locale = (await requestLocale) || DEFAULT_LOCALE;

  if (!isSupportedLocale(locale)) {
    return notFound();
  }

  try {
    const messages = await import(`../../messages/${locale}.json`);
    return {
      locale,
      messages: messages.default,
    };
  } catch (_error) {
    return notFound();
  }
});
