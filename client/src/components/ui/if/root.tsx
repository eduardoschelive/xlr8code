import type { ReactElement } from 'react'
import { Else } from './else'
import { Then } from './then'
import type { BooleanLike } from './types'

export type RootProps = {
  condititon: BooleanLike
  children:
    | ReactElement<typeof Then>
    | [ReactElement<typeof Then>, ReactElement<typeof Else>]
}

export const Root = ({ condititon, children }: RootProps) => {
  const childrenAsArray = Array.isArray(children) ? children : [children]

  const then = childrenAsArray.find((child) => child.type === Then)

  if (!then) {
    throw new Error('The If component must have a Then component')
  }

  const elseComponent =
    childrenAsArray.find((child) => child.type === Else) || null

  return condititon ? then : elseComponent
}
