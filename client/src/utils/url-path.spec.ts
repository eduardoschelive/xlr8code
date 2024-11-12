import { isRootPath } from '@/utils/url-path'
import { describe, expect, it } from 'vitest'

describe('url-path', () => {
  it('should return if it is the root path', () => {
    expect(isRootPath('/')).toBe(true)
  })

  it('should return if it is not the root path', () => {
    expect(isRootPath('/about')).toBe(false)
  })
})
