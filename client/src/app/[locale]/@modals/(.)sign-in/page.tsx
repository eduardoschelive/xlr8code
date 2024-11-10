import { RouteModal } from '@/components/ui/RouteModal'
import { ModalBody, ModalContent, ModalHeader } from '@nextui-org/modal'
import type { ReactNode } from 'react'

export default function SignInModal(): ReactNode {
  return (
    <RouteModal>
      <ModalContent>
        <ModalHeader>
          Sign In
        </ModalHeader>
        <ModalBody>
          <div>Login Modal</div>
        </ModalBody>
      </ModalContent>
    </RouteModal>
  )
}
