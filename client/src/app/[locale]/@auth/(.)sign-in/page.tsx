import { Modal, ModalBody, ModalContent } from '@nextui-org/modal'
import type { ReactNode } from 'react'

export default function SignInModal(): ReactNode {
  return (
    <Modal isOpen>
      <ModalContent>
        <ModalBody>
          <div>Login Modal</div>
        </ModalBody>
      </ModalContent>
    </Modal>
  )
}
