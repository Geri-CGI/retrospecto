import {fileURLToPath, URL} from 'node:url'

import {build, defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin
import vuetify from 'vite-plugin-vuetify'

import path from "path";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        vuetify({autoImport: true}),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        proxy: {
            "/api": {
                target: "http://localhost:8098",
                changeOrigin: true,
                secure: false,
            },
        },
        host: true
    },
    build: {
        outDir: path.resolve(__dirname, './target/dist'),
    }
})
