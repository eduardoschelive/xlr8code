import { NextIntlClientProvider } from 'next-intl'
import { getMessages } from 'next-intl/server'
import type { PropsWithChildren } from 'react'

export const LocaleProvider = async ({
  children,
}: PropsWithChildren): Promise<JSX.Element> => {
  const messages = await getMessages()

  return (
    <NextIntlClientProvider messages={messages}>
      {children}
    </NextIntlClientProvider>
  )
}
