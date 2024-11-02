import { Modal, ModalBody, ModalContent } from "@nextui-org/modal";

export default function SignInModal() {
  console.log(`[SignInModal]`)

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