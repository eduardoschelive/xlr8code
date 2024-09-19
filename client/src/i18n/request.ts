import { getRequestConfig } from 'next-intl/server'
import { notFound } from 'next/navigation'
import { isSupportedLocale } from './locale'

export default getRequestConfig(async ({ locale }) => {
  if (!isSupportedLocale(locale)) notFound()

  const { default: messages } = await import(`../../messages/${locale}.json`)

  return {
    messages,
  }
})
