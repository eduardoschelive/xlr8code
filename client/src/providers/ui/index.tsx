'use client'

import { useRouter } from '@/i18n/routing'
import { NextUIProvider } from '@nextui-org/system'
import type { PropsWithChildren, ReactNode } from 'react'

export const UIProvider = ({ children }: PropsWithChildren): ReactNode => {
  const { push } = useRouter()

  return <NextUIProvider navigate={push}>{children}</NextUIProvider>
}
