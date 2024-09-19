import { createSharedPathnamesNavigation } from 'next-intl/navigation'
import { defineRouting } from 'next-intl/routing'
import { SUPPORTED_LOCALES } from './locale'

export const routing = defineRouting({
  locales: SUPPORTED_LOCALES,
  defaultLocale: 'en-US',
})

export const { Link, redirect, usePathname, useRouter } =
  createSharedPathnamesNavigation(routing)
