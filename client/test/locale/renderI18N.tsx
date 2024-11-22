import {
  type RenderOptions,
  type RenderResult,
  render,
} from '@testing-library/react'
import { NextIntlClientProvider } from 'next-intl'
import type { PropsWithChildren, ReactNode } from 'react'
import { TEST_LOCALE } from './constants'

// biome-ignore lint/nursery/useComponentExportOnlyModules: Support testing with i18n provider without needing SSR
const ClientLocaleProvider = ({ children }: PropsWithChildren): ReactNode => {
  return <NextIntlClientProvider locale={TEST_LOCALE}>{children}</NextIntlClientProvider>
}

export const renderI18N = (
  ui: React.ReactNode,
  options?: Omit<RenderOptions, 'queries'> | undefined
): RenderResult => {
  return render(<ClientLocaleProvider>{ui}</ClientLocaleProvider>, {
    ...options,
  })
}
