import { Navbar } from '@/components/layout/Navbar'
import { ProvidersWrapper } from '@/providers/wrapper'
import '../globals.css'

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
