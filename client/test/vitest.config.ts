import react from '@vitejs/plugin-react'
import tsConfigPaths from 'vite-tsconfig-paths'
import { coverageConfigDefaults, defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: './test/vitest-setup.ts',
    alias: {
      '@/*': new URL('./src/', import.meta.url).pathname,
      '@test/*': new URL('./test/', import.meta.url).pathname
    },
    coverage: {
      exclude: [
        ...coverageConfigDefaults.exclude,
        '**/*.type.ts',
        '**/*.config.*',
      ],
    },
  },
  plugins: [tsConfigPaths(), react()],
})
