'use client'

import { useIsMounted } from '@/hooks/useIsMounted'
import { Button } from '@nextui-org/button'
import { useReducedMotion } from 'framer-motion'
import { useTheme } from 'next-themes'
import { type ReactNode, useRef } from 'react'
import { flushSync } from 'react-dom'
import { IoMoon, IoSunny } from 'react-icons/io5'
import { animateThemeTransition } from './animations'

export const ThemeSwitch = (): ReactNode => {
  const isMounted = useIsMounted()
  const buttonRef = useRef<HTMLButtonElement>(null)
  const { resolvedTheme, setTheme } = useTheme()
  const isReducedMotion = useReducedMotion()

  const isDarkTheme = resolvedTheme === 'dark'

  const handleThemeChange = async (): Promise<void> => {
    const newTheme = isDarkTheme ? 'light' : 'dark'
    const { current: currentButton } = buttonRef

    const canAnimate =
      currentButton && Boolean(document.startViewTransition) && !isReducedMotion

    if (!canAnimate) {
      setTheme(newTheme)
      return
    }

    /* v8 ignore start */
    // Can't find a way to test this block
    const transition = document.startViewTransition(() => {
      flushSync(() => {
        setTheme(newTheme)
      })
    })
    /* v8 ignore end */

    await transition.ready
    animateThemeTransition(currentButton)
  }

  if (!isMounted) {
    return null
  }

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
