<template>
  <div class="notice-container">
    <!-- 导航菜单 -->
    <el-menu
        :default-active="$route.path"
        class="el-menu-demo"
        mode="horizontal"
        :router="true"
    >
      <el-menu-item index="/front/home">系统首页</el-menu-item>
      <el-menu-item index="/front/course">精品课程</el-menu-item>
      <el-menu-item index="/front/home/frontnotice">公告</el-menu-item>
      <el-menu-item @click="logout">退出</el-menu-item>
    </el-menu>

    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><Bell /></el-icon>
          健身房公告
        </h1>
        <p class="page-subtitle">了解最新的健身房动态和重要通知</p>
      </div>
    </div>

    <!-- 公告列表 -->
    <div class="notice-content">
      <div class="notice-list">
        <el-empty v-if="data.noticeData.length === 0" description="暂无公告" />
        
        <div class="notice-card" v-for="(item, index) in data.noticeData" :key="item.id">
          <div class="notice-header">
            <div class="notice-title">
              <el-icon class="notice-icon"><Document /></el-icon>
              {{ item.title }}
            </div>
            <div class="notice-meta">
              <el-tag type="info" size="small">
                <el-icon><Clock /></el-icon>
                {{ item.time }}
              </el-tag>
            </div>
          </div>
          
          <div class="notice-body">
            <div class="notice-content-text">
              {{ item.content }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { useRouter } from 'vue-router';
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { 
  Bell, 
  Document, 
  Clock
} from '@element-plus/icons-vue';

const router = useRouter();

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  noticeData: [],
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200'){
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()

const logout = () => {
  localStorage.removeItem('quan_user')
  router.push('/login')
}
</script>

<style scoped>
.notice-container {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

.el-menu-demo {
  border-bottom: none;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.page-header {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
  padding: 60px 0;
  text-align: center;
  color: white;
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.title-icon {
  font-size: 2.2rem;
  color: #ffd700;
}

.page-subtitle {
  font-size: 1.1rem;
  opacity: 0.9;
  margin: 0;
  font-weight: 300;
}

.notice-content {
  padding: 40px 20px;
  max-width: 900px;
  margin: 0 auto;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.notice-card {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.notice-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15);
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  gap: 16px;
}

.notice-title {
  font-size: 1.4rem;
  font-weight: 600;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  line-height: 1.4;
}

.notice-icon {
  color: #667eea;
  font-size: 1.3rem;
  flex-shrink: 0;
}

.notice-meta {
  flex-shrink: 0;
}

.notice-body {
  /* 移除底部边距，因为不再有footer */
}

.notice-content-text {
  font-size: 1rem;
  line-height: 1.7;
  color: #5a6c7d;
  text-align: justify;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.el-tag {
  border: none;
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  font-weight: 500;
}

.el-empty {
  padding: 60px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .notice-content {
    padding: 20px 16px;
  }
  
  .notice-card {
    padding: 20px;
  }
  
  .notice-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .notice-title {
    font-size: 1.2rem;
  }
}
</style>