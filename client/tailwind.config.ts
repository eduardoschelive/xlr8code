import { nextui } from '@nextui-org/theme'
import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
    './node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        crust: 'var(--crust)',
        mantle: 'var(--mantle)',
        base: 'var(--base)',
      },
    },
  },
  darkMode: 'class',
  plugins: [
    nextui({
      themes: {
        dark: {
          colors: {
            background: '#11111B', // Crust
            foreground: '#cdd6f4', // Text
            divider: '#cba6f7', // Mauve
            overlay: '#181825', // Mantle
            focus: '#585b70', // Surface 2
            content1: '#181825', // Mantle,
            content2: '#313244', // Surface 0
            content3: '#45475a', // Surface 1
            content4: '#585b70', // Surface 2,
            primary: '#cba6f7', // Mauve
            secondary: '#b4befe', // Lavender
            success: '#a6e3a1', // Green
            warning: '#f9e2af', // Yellow
            danger: '#f38ba8', // Red
            default: {
              DEFAULT: '#11111b', // Crust
              foreground: '#cdd6f4', // Text
            },
          },
        },
      },
    }),
  ],
}
export default config
