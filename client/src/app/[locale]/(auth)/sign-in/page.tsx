import { Login, LoginTitle } from '@/components/layout/Login'
import { RouteModal } from '@/components/ui/RouteModal'
import { ModalBody, ModalContent } from '@nextui-org/modal'
import { useTranslations } from 'next-intl'
import type { ReactNode } from 'react'

export default function SignInPage(): ReactNode {
  const t = useTranslations()

  return (
    <RouteModal placement="top">
      <ModalContent>
        <LoginTitle />
        <ModalBody>
          <Login />
        </ModalBody>
      </ModalContent>
    </RouteModal>
  )
}
