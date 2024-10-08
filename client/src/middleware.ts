import createMiddleware from 'next-intl/middleware'
import type { NextRequest } from 'next/server'
import {
  APPLICATION_LOCALE_COOKIE_NAME,
  NEXT_LOCALE_COOKIE_NAME,
} from './i18n/constants'
import { isSupportedLocale } from './i18n/locale'
import { routing } from './i18n/routing'
import { deleteCookie, getCookieValue, setCookie } from './utils/cookie'
import { isRootPath } from './utils/url-path'

const handleI18nRouting = createMiddleware(routing)

const handleI18nRoutingWithCustomCookie = (request: NextRequest) => {
  const cookieValue = getCookieValue(request, APPLICATION_LOCALE_COOKIE_NAME)
  setCookie(request, NEXT_LOCALE_COOKIE_NAME, cookieValue)

  const response = handleI18nRouting(request)

  const newCookieValue = getCookieValue(response, NEXT_LOCALE_COOKIE_NAME)
  if (newCookieValue) {
    deleteCookie(response, NEXT_LOCALE_COOKIE_NAME)
    setCookie(response, APPLICATION_LOCALE_COOKIE_NAME, newCookieValue)
  }

  return response
}

export default function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl

  const locale = pathname.split('/')[1]

  if (!isRootPath(pathname) && !isSupportedLocale(locale)) {
    return
  }

  const response = handleI18nRoutingWithCustomCookie(request)

  return response
}
