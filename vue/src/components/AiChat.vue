<template>
  <!-- 悬浮按钮 -->
  <div class="ai-float-btn" @click="toggle" title="AI健身顾问">
    <el-icon :size="26"><ChatDotRound /></el-icon>
  </div>

  <!-- 聊天窗口 -->
  <div class="ai-chat-box" v-if="visible">
    <div class="ai-chat-header">
      <span>🤖 AI 健身顾问</span>
      <el-icon style="cursor:pointer" @click="visible=false"><Close /></el-icon>
    </div>

    <div class="ai-chat-messages" ref="msgBox">
      <div v-for="(msg, i) in messages" :key="i"
           :class="['ai-msg', msg.role === 'user' ? 'ai-msg-user' : 'ai-msg-ai']">
        <span>{{ msg.content }}</span>
      </div>
      <div v-if="loading" class="ai-msg ai-msg-ai">
        <span>正在思考中...</span>
      </div>
    </div>

    <div class="ai-chat-input">
      <el-input v-model="input" placeholder="输入问题，回车发送"
                @keyup.enter="send" :disabled="loading" size="small" />
      <el-button type="primary" size="small" @click="send" :loading="loading">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ChatDotRound, Close } from '@element-plus/icons-vue'
import request from '@/utils/request.js'

const visible = ref(false)
const input = ref('')
const loading = ref(false)
const msgBox = ref(null)
const messages = ref([
  { role: 'ai', content: '你好！我是AI健身顾问，有什么可以帮你的？' }
])

const toggle = () => { visible.value = !visible.value }

const scrollBottom = () => {
  nextTick(() => {
    if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
  })
}

const send = async () => {
  const msg = input.value.trim()
  if (!msg || loading.value) return
  messages.value.push({ role: 'user', content: msg })
  input.value = ''
  loading.value = true
  scrollBottom()
  try {
    const res = await request.post('/ai/chat', { message: msg })
    if (res.code === '200') {
      messages.value.push({ role: 'ai', content: res.data })
    } else {
      messages.value.push({ role: 'ai', content: '抱歉，出了点问题，请稍后再试。' })
    }
  } catch {
    messages.value.push({ role: 'ai', content: '网络异常，请稍后再试。' })
  } finally {
    loading.value = false
    scrollBottom()
  }
}
</script>

<style scoped>
.ai-float-btn {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: #7F55B1;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  z-index: 9999;
  transition: background 0.2s;
}
.ai-float-btn:hover { background: #9B7EBD; }

.ai-chat-box {
  position: fixed;
  right: 30px;
  bottom: 95px;
  width: 340px;
  height: 460px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0,0,0,0.15);
  display: flex;
  flex-direction: column;
  z-index: 9998;
  overflow: hidden;
}
.ai-chat-header {
  background: #7F55B1;
  color: #fff;
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
.ai-chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.ai-msg {
  max-width: 80%;
  padding: 8px 12px;
  border-radius: 10px;
  font-size: 13px;
  line-height: 1.5;
  word-break: break-word;
}
.ai-msg-user {
  align-self: flex-end;
  background: #7F55B1;
  color: #fff;
  border-bottom-right-radius: 2px;
}
.ai-msg-ai {
  align-self: flex-start;
  background: #f3f0f8;
  color: #333;
  border-bottom-left-radius: 2px;
}
.ai-chat-input {
  padding: 10px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 8px;
}
</style>
