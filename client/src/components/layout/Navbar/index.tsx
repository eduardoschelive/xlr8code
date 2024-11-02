import { ThemeSwitch } from "../ThemeSwitch"

export const Navbar = () => {
  return (
    <header className='flex items-center justify-between bg-base p-4'>
      <h1 className='font-bold text-2xl text-white'>Next.js Starter</h1>
      <nav>
        <ThemeSwitch />
      </nav>
    </header>
  )
}