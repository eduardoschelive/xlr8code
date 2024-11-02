import { getRequestConfig } from 'next-intl/server'
import { notFound } from 'next/navigation'
import { DEFAULT_LOCALE, isSupportedLocale } from './locale'

export default getRequestConfig(async ({ requestLocale }) => {
  const locale = (await requestLocale) || DEFAULT_LOCALE

  if (!isSupportedLocale(locale)) { notFound() }

  const { default: messages } = await import(`../../messages/${locale}.json`)

  return {
    locale,
    messages,
  }
})
