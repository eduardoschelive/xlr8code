'use client'

import { useIsMounted } from '@/hooks/useIsMounted'
import { Button } from '@nextui-org/button'
import { useReducedMotion } from 'framer-motion'
import { useTheme } from 'next-themes'
import { useRef } from 'react'
import { flushSync } from 'react-dom'
import { IoMoon, IoSunny } from 'react-icons/io5'
import { animateThemeTransition } from './animations'

export const ThemeSwitch = () => {
  const isMounted = useIsMounted()
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { resolvedTheme, setTheme } = useTheme()
  const isReducedMotion = useReducedMotion()

  const isDarkTheme = resolvedTheme === 'dark'

  const handleThemeChange = async () => {
    const newTheme = isDarkTheme ? 'light' : 'dark'
    const { current: currentButton } = buttonRef

    if (!currentButton || !document.startViewTransition || isReducedMotion) {
      setTheme(newTheme)
      return
    }

    const transition = document.startViewTransition(() => {
      flushSync(() => {
        setTheme(newTheme)
      })
    })

    await transition.ready
    animateThemeTransition(currentButton)
  }

  if (!isMounted) return null

  return (
    <Button
      isIconOnly
      onClick={handleThemeChange}
      radius="full"
      variant="light"
      ref={buttonRef}
    >
      {isDarkTheme ? <IoMoon size={24} /> : <IoSunny size={24} />}
    </Button>
  )
}
