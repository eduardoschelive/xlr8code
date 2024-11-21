import tsconfigPaths from 'vite-tsconfig-paths';
import react from '@vitejs/plugin-react'
import { defineConfig, coverageConfigDefaults } from 'vitest/config';

export default defineConfig({
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: './test/vitest-setup.ts',
    alias: {
      '@/*': new URL('./src/', import.meta.url).pathname,
    },
    coverage: {
      exclude: [
        ...coverageConfigDefaults.exclude,
        '**/*.type.ts',
        '**/*.config.*',
      ],
    }
  },
  plugins: [tsconfigPaths(), react()]
})