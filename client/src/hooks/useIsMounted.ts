import { useEffect, useState } from 'react'

export const useIsMounted = () => {
  const [isMounted, setMounted] = useState(false)
  useEffect(() => {
    setMounted(true)
  }, [])

  return isMounted
}
