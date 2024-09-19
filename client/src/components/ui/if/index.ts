import { Else } from './else'
import type { RootProps } from './root'
import { Root } from './root'
import { Then } from './then'

export type { BooleanLike } from './types'
export type { RootProps as IfProps }

export const If = {
  Root,
  Then,
  Else,
}
