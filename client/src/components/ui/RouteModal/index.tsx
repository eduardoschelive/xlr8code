'use client'

import { useRouter } from '@/i18n/routing'
import { Modal, type ModalProps } from '@nextui-org/modal'
import type { ReactNode } from 'react'

type RouteModalProps = {} & ModalProps

export const RouteModal = ({ children }: RouteModalProps): ReactNode => {
  const { back } = useRouter()

  const handleCloseRouteModal = (): void => {
    back()
  }

  // Route modals are always open
  return (
    <Modal isOpen onClose={handleCloseRouteModal}>
      {children}
    </Modal>
  )
}
