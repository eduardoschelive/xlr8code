const ANIMATION_DURATION = 500
const ANIMATION_EASING = 'ease-in-out'

interface AnimationProps {
  xAxis: number
  yAxis: number
  maxRadius: number
}

const calculateAnimationProperties = (button: HTMLElement): AnimationProps => {
  const { top, left, width, height } = button.getBoundingClientRect()
  const xAxis = left + width / 2
  const yAxis = top + height / 2
  const maxRadius = Math.hypot(
    Math.max(left, window.innerWidth - left),
    Math.max(top, window.innerHeight - top)
  )

  return { xAxis, yAxis, maxRadius }
}

const animateGrowingCircle = ({
  xAxis,
  yAxis,
  maxRadius,
}: AnimationProps): void => {
  document.documentElement.animate(
    {
      clipPath: [
        `circle(0px at ${xAxis}px ${yAxis}px)`,
        `circle(${maxRadius}px at ${xAxis}px ${yAxis}px)`,
      ],
    },
    {
      duration: ANIMATION_DURATION,
      easing: ANIMATION_EASING,
      pseudoElement: '::view-transition-new(root)',
    }
  )
}

export const animateThemeTransition = (
  sourceElement: HTMLButtonElement
): void => {
  const animationProps = calculateAnimationProperties(sourceElement)
  animateGrowingCircle(animationProps)
}
