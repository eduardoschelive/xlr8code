import type { ReactNode } from 'react'
import { ThemeSwitch } from '../ThemeSwitch'
import { UserIcon } from '../UserIcon'

export const Navbar = (): ReactNode => {
  return (
    <header className="flex items-center justify-between bg-base p-4">
      <h1 className="font-bold text-2xl text-white">xlr8code</h1>
      <nav>
        <ThemeSwitch />
        <UserIcon />
      </nav>
    </header>
  )
}
