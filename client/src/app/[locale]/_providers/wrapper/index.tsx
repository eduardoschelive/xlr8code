import { PropsWithChildren } from 'react'
import { LocaleProvider } from '../locale'
import { ThemeProvider } from '../theme'
import { UIProvider } from '../ui'

export const ProvidersWrapper = ({ children }: PropsWithChildren) => {
  return (
    <LocaleProvider>
      <ThemeProvider>
        <UIProvider>{children}</UIProvider>
      </ThemeProvider>
    </LocaleProvider>
  )
}
