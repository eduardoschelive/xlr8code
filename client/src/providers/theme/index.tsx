import { SUPPORTED_THEMES } from '@/utils/theme'
import { ThemeProvider as NextThemesProvider } from 'next-themes'
import type { PropsWithChildren, ReactNode } from 'react'

export const ThemeProvider = ({ children }: PropsWithChildren): ReactNode => {
  return (
    <NextThemesProvider
      attribute="class"
      enableColorScheme
      disableTransitionOnChange
      themes={SUPPORTED_THEMES}
    >
      {children}
    </NextThemesProvider>
  )
}
