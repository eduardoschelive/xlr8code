import { Navbar } from '@/components/layout/Navbar'
import { ProvidersWrapper } from '@/providers/wrapper'
import type { PropsWithChildren, ReactNode } from 'react'
import '../globals.css'

type RootLayoutProps = {
  params: Promise<{ locale: string }>
  modals: ReactNode
}

export default async function RootLayout({
  children,
  modals,
  params,
}: PropsWithChildren<RootLayoutProps>): Promise<ReactNode> {
  const { locale } = await params
  return (
    <html lang={locale} suppressHydrationWarning>
      <body className="bg-crust">
        <ProvidersWrapper>
          <Navbar />
          {children}
          {modals}
        </ProvidersWrapper>
      </body>
    </html>
  )
}
