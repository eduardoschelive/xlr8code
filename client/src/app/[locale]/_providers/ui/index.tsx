import { NextUIProvider } from '@nextui-org/system'
import { PropsWithChildren } from 'react'

export const UIProvider = ({ children }: PropsWithChildren) => {
  return <NextUIProvider>{children}</NextUIProvider>
}
