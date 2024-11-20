import { getRequestConfig } from 'next-intl/server'
import { notFound } from 'next/navigation'
import { DEFAULT_LOCALE } from './locale'

export default getRequestConfig(async ({ requestLocale }) => {
  const locale = (await requestLocale) || DEFAULT_LOCALE

  try {
    const messages = await import(`../../messages/${locale}.json`)
    return {
      locale,
      messages: messages.default,
    }
  } catch (_error) {
    return notFound()
  }
})
