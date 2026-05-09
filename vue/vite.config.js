import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
      //按需导入
      AutoImport({
        resolvers:[ElementPlusResolver()],
      }),
      Components({
        resolvers:[
            //配置elementPlus采用sass样式配置系统
            ElementPlusResolver({importStyle:'sass'})
        ],
      }),
  ],
  css:{
    preprocessorOptions:{
      scss:{
        additionalData:'@use "@/assets/css/index.scss" as *;',
      },
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
