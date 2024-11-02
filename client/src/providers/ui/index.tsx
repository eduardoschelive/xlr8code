import { NextUIProvider } from '@nextui-org/system'
import type { PropsWithChildren } from 'react'

export const UIProvider = ({ children }: PropsWithChildren) => {
  return <NextUIProvider>{children}</NextUIProvider>
}
