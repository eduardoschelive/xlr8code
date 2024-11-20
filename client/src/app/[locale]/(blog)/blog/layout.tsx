import { Navbar } from '@/components/layout/Navbar'
import type { PropsWithChildren, ReactNode } from 'react'

export default function Layout({ children }: PropsWithChildren): ReactNode {
  return (
    <>
      <Navbar />
      {children}
    </>
  )
}
