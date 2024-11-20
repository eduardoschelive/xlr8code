import { isRootPath } from '@/utils/url'
import { describe, expect, it } from 'vitest'

describe('url-path', () => {
  it('should return if it is the root path', () => {
    expect(isRootPath('/')).toBe(true)
  })

  it('should return if it is not the root path', () => {
    expect(isRootPath('/about')).toBe(false)
  })

  describe('url-path', () => {
    it('should return true if it is the root path', () => {
      expect(isRootPath('/')).toBe(true)
    })

    it('should return false if it is not the root path', () => {
      expect(isRootPath('/about')).toBe(false)
    })

    it('should return false for an empty string', () => {
      expect(isRootPath('')).toBe(false)
    })

    it('should return false for a path with only a slash and characters', () => {
      expect(isRootPath('/home')).toBe(false)
    })

    it('should return false for a path with multiple slashes', () => {
      expect(isRootPath('/home/user')).toBe(false)
    })
  })
})
