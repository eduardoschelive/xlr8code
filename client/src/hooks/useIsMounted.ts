import { useEffect, useState } from 'react'

export const useIsMounted = (): boolean => {
  const [isMounted, setMounted] = useState(false)
  useEffect(() => {
    setMounted(true)
  }, [])

  return isMounted
}
