import { useTranslations } from 'next-intl'

export default function Home() {
  const t = useTranslations('Home')

  return new Array(100).fill(1).map(x => {
    return <div key={x}>{t('title')}</div>
  })
}
