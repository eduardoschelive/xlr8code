import type { PropsWithChildren, ReactNode } from 'react'
import { LocaleProvider } from '../locale'
import { ThemeProvider } from '../theme'
import { UIProvider } from '../ui'

export const ProvidersWrapper = ({ children }: PropsWithChildren): ReactNode => {
  return (
    <LocaleProvider>
      <ThemeProvider>
        <UIProvider>{children}</UIProvider>
      </ThemeProvider>
    </LocaleProvider>
  )
}
