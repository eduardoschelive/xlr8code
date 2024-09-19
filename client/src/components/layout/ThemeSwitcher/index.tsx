'use client'

import { useIsMounted } from '@/hooks/useIsMounted'
import { useTheme } from 'next-themes'

export const ThemeSwitcher = () => {
  const isMounted = useIsMounted()
  const { theme, setTheme } = useTheme()

  if (!isMounted) {
    return null
  }

  return (
    <select value={theme} onChange={e => setTheme(e.target.value)}>
      <option value="system">System</option>
      <option value="dark">Dark</option>
      <option value="light">Light</option>
    </select>
  )
}
