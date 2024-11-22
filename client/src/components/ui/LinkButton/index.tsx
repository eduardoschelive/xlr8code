'use client'

import { Link } from '@/i18n/routing'
import { Button, type ButtonProps } from '@nextui-org/button'
import type { PropsWithChildren, ReactNode } from 'react'

type LinkButtonProps = {
  href: string
} & ButtonProps

export const LinkButton = ({
  children,
  href,
  ...props
}: PropsWithChildren<LinkButtonProps>): ReactNode => {
  return (
    // biome-ignore lint/a11y/useSemanticElements: This is a button that acts as a link
    <Button as={Link} href={href} {...props} role="link">
      {children}
    </Button>
  )
}
