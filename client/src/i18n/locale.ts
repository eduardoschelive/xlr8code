export type Locale = 'en-US' | 'pt-BR'

export const DEFAULT_LOCALE: Locale = 'en-US'
export const SUPPORTED_LOCALES: Locale[] = [DEFAULT_LOCALE, 'pt-BR']

export const isSupportedLocale = (locale: string): locale is Locale => {
  return SUPPORTED_LOCALES.includes(locale as Locale)
}
