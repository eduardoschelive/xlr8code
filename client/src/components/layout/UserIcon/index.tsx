'use client'

import { Link } from '@/i18n/routing'
import { Button } from '@nextui-org/button'
import type { ReactNode } from 'react'
import { FaUserCircle } from 'react-icons/fa'

export const UserIcon = (): ReactNode => {
  return (
    <Button isIconOnly radius="full" variant="light" as={Link} href="/sign-in">
      <FaUserCircle size={24} />
    </Button>
  )
}
