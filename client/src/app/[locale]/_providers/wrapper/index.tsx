import { PropsWithChildren } from 'react'
import { LocaleProvider } from '../locale'
import { ThemeProvider } from '../theme'

export const ProvidersWrapper = ({ children }: PropsWithChildren) => {
  return (
    <LocaleProvider>
      <ThemeProvider>{children}</ThemeProvider>
    </LocaleProvider>
  )
}
