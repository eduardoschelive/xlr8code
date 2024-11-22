import { renderI18N } from '@test/locale/renderI18N'
import { withLocalePath } from '@test/locale/url'
import { screen } from '@testing-library/react'
import { describe, expect, it } from 'vitest'
import { UserIcon } from '.'

describe('UserIcon', () => {
  it('renders correctly', () => {
    renderI18N(<UserIcon />)

    expect(screen.getByRole('link')).toBeInTheDocument()
  })

  it('should redirect to the signIn page', () => {
    renderI18N(<UserIcon />)

    const link = screen.getByRole('link')

    const expectedAttributeValue = withLocalePath('/sign-in')

    expect(link).toHaveAttribute('href', expectedAttributeValue)
  })
})
