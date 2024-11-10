import { LinkButton } from '@/components/ui/LinkButton'
import type { ReactNode } from 'react'
import { FaUserCircle } from 'react-icons/fa'

export const UserIcon = (): ReactNode => {
  return (
    <LinkButton isIconOnly radius="full" variant="light" href="/sign-in">
      <FaUserCircle size={24} />
    </LinkButton>
  )
}
