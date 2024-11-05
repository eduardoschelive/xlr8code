import { Navbar } from '@/components/layout/Navbar'
import { ProvidersWrapper } from '@/providers/wrapper'
import type { PropsWithChildren, ReactNode } from 'react'
import '../globals.css'

type RootLayoutProps = {
  params: Promise<{ locale: string }>
  auth: ReactNode
}

export default async function RootLayout({
  children,
  auth,
  params,
}: PropsWithChildren<RootLayoutProps>): Promise<ReactNode> {
  const { locale } = await params
  return (
    <html lang={locale} suppressHydrationWarning>
      <body className="bg-crust">
        <ProvidersWrapper>
          <Navbar />
          {children}
          {auth}
        </ProvidersWrapper>
      </body>
    </html>
  )
}
