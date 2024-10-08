import type { NextRequest, NextResponse } from 'next/server'

export type CookieCapableObject = NextRequest | NextResponse

export function getCookieValue(
  object: CookieCapableObject,
  cookieName: string,
  defaultValue = ''
): string {
  return object.cookies.get(cookieName)?.value || defaultValue
}

export function setCookie(
  object: CookieCapableObject,
  cookieName: string,
  cookieValue: string
): void {
  object.cookies.set(cookieName, cookieValue)
}

export function deleteCookie(
  object: CookieCapableObject,
  cookieName: string
): void {
  object.cookies.delete(cookieName)
}
