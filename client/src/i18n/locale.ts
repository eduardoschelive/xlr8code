export type Locale = 'en-US' | 'pt-BR'

export const SUPPORTED_LOCALES: Locale[] = ['en-US', 'pt-BR']

export const isSupportedLocale = (locale: string): locale is Locale => {
  return SUPPORTED_LOCALES.includes(locale as Locale)
}
