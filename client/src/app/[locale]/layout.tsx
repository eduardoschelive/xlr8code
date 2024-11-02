import { Navbar } from '@/components/layout/Navbar'
import { ProvidersWrapper } from '@/providers/wrapper'
import type { PropsWithChildren } from 'react'
import '../globals.css'

type RootLayoutProps = {
  params: Promise<{ locale: string }>
  auth: React.ReactNode
}

export default async function RootLayout({
  children,
  auth,
  params,
}: PropsWithChildren<RootLayoutProps>) {
  const { locale } = await params
  return (
    <html lang={locale} suppressHydrationWarning>
      <body className="bg-crust">
        <ProvidersWrapper>
          <Navbar />
          {auth}
          {children}
        </ProvidersWrapper>
      </body>
    </html>
  )
}
