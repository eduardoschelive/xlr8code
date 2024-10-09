import { SUPPORTED_THEMES } from '@/utils/theme'
import { ThemeProvider as NextThemesProvider } from 'next-themes'
import type { PropsWithChildren } from 'react'

export const ThemeProvider = ({ children }: PropsWithChildren) => {
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
