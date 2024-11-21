import { render, screen, fireEvent } from '@testing-library/react'
import { ThemeSwitch } from './index'
import { useTheme } from 'next-themes'
import { useIsMounted } from '@/hooks/useIsMounted'
import { beforeEach, describe, expect, it, vi } from 'vitest'

// Mock hooks
vi.mock('next-themes', () => ({
  useTheme: vi.fn(),
}))

vi.mock('@/hooks/useIsMounted', () => ({
  useIsMounted: vi.fn(),
}))

describe('ThemeSwitch', () => {
  const mockUseIsMounted = (isMounted: boolean): void => {
    vi.mocked(useIsMounted).mockReturnValue(isMounted)
  }

  const mockUseTheme = (resolvedTheme: string, setTheme = vi.fn()): void => {
    vi.mocked(useTheme).mockReturnValue({
      resolvedTheme,
      setTheme,
      themes: ['light', 'dark'],
    })
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders correctly when mounted', () => {
    mockUseIsMounted(true)
    mockUseTheme('light')

    render(<ThemeSwitch />)

    expect(screen.getByRole('button')).toBeInTheDocument()
  })

  it('does not render when not mounted', () => {
    mockUseIsMounted(false)

    render(<ThemeSwitch />)

    expect(screen.queryByRole('button')).toBeNull()
  })

  it('changes theme to dark on light mode', () => {
    const setThemeMock = vi.fn()
    mockUseIsMounted(true)
    mockUseTheme('light', setThemeMock)

    render(<ThemeSwitch />)

    const button = screen.getByRole('button')
    fireEvent.click(button)

    expect(setThemeMock).toHaveBeenCalledWith('dark')
  })

  it('changes theme to light on dark mode', () => {
    const setThemeMock = vi.fn()
    mockUseIsMounted(true)
    mockUseTheme('dark', setThemeMock)

    render(<ThemeSwitch />)

    const button = screen.getByRole('button')
    fireEvent.click(button)

    expect(setThemeMock).toHaveBeenCalledWith('light')
  })

  it('does not change theme when not mounted', () => {
    const setThemeMock = vi.fn()
    mockUseIsMounted(false)
    mockUseTheme('light', setThemeMock)

    render(<ThemeSwitch />)

    const button = screen.queryByRole('button')
    expect(button).toBeNull()
  })

})