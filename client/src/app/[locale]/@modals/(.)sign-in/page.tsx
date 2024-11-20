import { Login } from '@/components/layout/Login'
import { RouteModal } from '@/components/ui/RouteModal'
import { ModalBody, ModalContent, ModalHeader } from '@nextui-org/modal'
import type { ReactNode } from 'react'

export default function SignInModal(): ReactNode {
  return (
    <RouteModal placement='top'>
      <ModalContent>
        <ModalHeader>Sign In</ModalHeader>
        <ModalBody>
          <Login />
        </ModalBody>
      </ModalContent>
    </RouteModal>
  )
}
