import { useTranslations } from 'next-intl'
import type { ReactNode } from 'react'

export const LoginTitle = (): ReactNode => {
  const t = useTranslations('Auth')

  return <div>{t('signIn')}</div>
}
