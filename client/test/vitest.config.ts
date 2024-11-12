import tsconfigPaths from 'vite-tsconfig-paths';
import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: './test/vitest-setup.ts',
    alias: {
      '@/*': new URL('./src/', import.meta.url).pathname,
    },
  },
  plugins: [tsconfigPaths()]
})