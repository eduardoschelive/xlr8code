import { isSupportedLocale } from '@/i18n/locale'
import { routing } from '@/i18n/routing'
import { isRootPath } from '@/utils/url'
import createMiddleware from 'next-intl/middleware'
import { type NextRequest, NextResponse } from 'next/server'

const handleI18nRouting = createMiddleware(routing)

export default function middleware(
  request: NextRequest
): NextResponse<unknown> {
  const { pathname } = request.nextUrl

  const locale = pathname.split('/')[1]

  const isValidRoute = isRootPath(pathname) || isSupportedLocale(locale)

  if (!isValidRoute) {
    return NextResponse.next()
  }

  const response = handleI18nRouting(request)

  return response
}
