import { Navbar } from '@/components/layout/Navbar'
import '../globals.css'
import { ProvidersWrapper } from './_providers/wrapper'

export default function RootLayout({
  children,
  params: { locale },
}: Readonly<{
  children: React.ReactNode
  params: { locale: string }
}>) {
  return (
    <html lang={locale} suppressHydrationWarning>
      <body className="bg-crust">
        <ProvidersWrapper>
          <Navbar />
          {children}
        </ProvidersWrapper>
      </body>
    </html>
  )
}
