'use client'

import { useIsMounted } from '@/hooks/useIsMounted'
import { Button } from '@nextui-org/button'
import { useTheme } from 'next-themes'
import { useCallback, useRef } from 'react'
import { flushSync } from 'react-dom'
import { IoMoon, IoSunny } from "react-icons/io5"

export const ThemeSwitcher = () => {
  const isMounted = useIsMounted()
  const ref = useRef<HTMLButtonElement>(null)
  const { resolvedTheme, setTheme } = useTheme()

  const isDarkTheme = resolvedTheme === 'dark'

  const handleThemeChange = useCallback(async () => {
    if (
      !ref.current ||
      !document.startViewTransition ||
      window.matchMedia('(prefers-reduced-motion: reduce)').matches
    ) {
      setTheme(isDarkTheme ? 'light' : 'dark')
      return
    }

    await document.startViewTransition(() => {
      flushSync(() => {
        setTheme(isDarkTheme ? 'light' : 'dark')
      })
    }).ready

    const { top, left, width, height } = ref.current.getBoundingClientRect()
    const x = left + width / 2
    const y = top + height / 2
    const right = window.innerWidth - left
    const bottom = window.innerHeight - top
    const maxRadius = Math.hypot(Math.max(left, right), Math.max(top, bottom))

    document.documentElement.animate(
      {
        clipPath: [
          `circle(0px at ${x}px ${y}px)`,
          `circle(${maxRadius}px at ${x}px ${y}px)`,
        ],
      },
      {
        duration: 500,
        easing: 'ease-in-out',
        pseudoElement: '::view-transition-new(root)',
      }
    )
  }, [isDarkTheme, setTheme])

  if (!isMounted) {
    return null
  }

  return (
    <Button isIconOnly onClick={handleThemeChange} radius="full" variant="light" ref={ref}>
      {isDarkTheme ? <IoMoon size={24} /> : <IoSunny size={24} />}
    </Button>
  )
}
