import { ThemeSwitch } from '../ThemeSwitcher'

export const Navbar = async () => {
  return (
    <header className="bg-base flex items-center justify-between p-4">
      <h1 className="text-2xl font-bold text-white">Next.js Starter</h1>
      <nav>
        <ThemeSwitch />
      </nav>
    </header>
  )
}
