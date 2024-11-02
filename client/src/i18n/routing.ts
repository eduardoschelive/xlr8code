import { createNavigation } from 'next-intl/navigation'
import { defineRouting } from 'next-intl/routing'
import { SUPPORTED_LOCALES } from './locale'

export const routing = defineRouting({
  locales: SUPPORTED_LOCALES,
  defaultLocale: 'en-US',
  localePrefix: 'always'
})

export const { Link, redirect, usePathname, useRouter } =
  createNavigation(routing)
