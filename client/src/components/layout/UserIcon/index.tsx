'use client'

import { Link } from '@/i18n/routing'
import { Button } from '@nextui-org/button'
import { FaUserCircle } from 'react-icons/fa'

export const UserIcon = () => {
  return (
    <Link href="/sign-in" passHref>
      <Button isIconOnly radius="full" variant="light">
        <FaUserCircle size={24} />
      </Button>
    </Link>
  )
}
