<template>
  <div class="course-container">
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
          <el-icon class="title-icon"><Trophy /></el-icon>
          精品课程
        </h1>
        <p class="page-subtitle">专业教练指导，科学健身训练</p>
      </div>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-section">
      <div class="filter-content">
        <el-row :gutter="20" align="middle">
          <el-col :span="6">
            <el-select v-model="data.filterCategory" placeholder="选择分类" @change="filterCourses" clearable>
              <el-option label="全部分类" value="" />
              <el-option v-for="category in data.categories" :key="category.id" :label="category.name" :value="category.id" />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-slider
                v-model="data.priceRange"
                range
                :max="1000"
                :min="0"
                :step="50"
                show-stops
                @change="filterCourses"
            />
            <div class="price-label">价格范围: ¥{{ data.priceRange[0] }} - ¥{{ data.priceRange[1] }}</div>
          </el-col>
          <el-col :span="6">
            <el-input v-model="data.searchKeyword" placeholder="搜索课程名称" @input="filterCourses" clearable>
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="resetFilters">重置筛选</el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 课程列表 -->
    <div class="course-content">
      <el-empty v-if="data.filteredCourses.length === 0" description="暂无课程" />
      
      <div class="course-grid" v-else>
        <div class="course-card" v-for="course in data.filteredCourses" :key="course.id">
          <div class="course-image">
            <img :src="course.image || '/src/assets/imgs/bg.png'" :alt="course.name" />
            <div class="course-status" :class="course.status === 'ACTIVE' ? 'active' : 'disabled'">
              {{ course.status === 'ACTIVE' ? '可预约' : '暂停' }}
            </div>
          </div>
          
          <div class="course-info">
            <h3 class="course-title">{{ course.name }}</h3>
            <p class="course-description">{{ course.description }}</p>
            
            <div class="course-details">
              <div class="detail-item">
                <el-icon><User /></el-icon>
                <span>{{ course.coachName }}</span>
              </div>
              <div class="detail-item">
                <el-icon><Clock /></el-icon>
                <span>{{ course.duration }}分钟</span>
              </div>
              <div class="detail-item">
                <el-icon><UserFilled /></el-icon>
                <span>最多{{ course.maxParticipants }}人</span>
              </div>
            </div>
            
            <div class="course-footer">
              <div class="course-price">¥{{ course.price }}</div>
              <el-button 
                  type="primary" 
                  :disabled="course.status !== 'ACTIVE' || !data.user.id"
                  @click="bookCourse(course)"
              >
                {{ !data.user.id ? '请先登录' : (course.status === 'ACTIVE' ? '立即预约' : '暂停预约') }}
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 预约确认对话框 -->
    <el-dialog v-model="data.bookingVisible" title="课程预约确认" width="500px">
      <div v-if="data.selectedCourse">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="课程名称">{{ data.selectedCourse.name }}</el-descriptions-item>
          <el-descriptions-item label="授课教练">{{ data.selectedCourse.coachName }}</el-descriptions-item>
          <el-descriptions-item label="课程时长">{{ data.selectedCourse.duration }}分钟</el-descriptions-item>
          <el-descriptions-item label="课程价格">¥{{ data.selectedCourse.price }}</el-descriptions-item>
          <el-descriptions-item label="课程描述">{{ data.selectedCourse.description }}</el-descriptions-item>
        </el-descriptions>
        
        <div style="margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 8px;">
          <p style="margin: 0; color: #666; font-size: 14px;">
            <el-icon><InfoFilled /></el-icon>
            预约提交后，请等待管理员审核。审核通过后，系统将自动发送邮件通知。
          </p>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="data.bookingVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBooking">确认预约</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import { useRouter } from 'vue-router';
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { 
  Trophy, 
  Search, 
  User, 
  Clock, 
  UserFilled,
  InfoFilled
} from '@element-plus/icons-vue';

const router = useRouter();

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  courses: [],
  filteredCourses: [],
  categories: [],
  filterCategory: '',
  priceRange: [0, 1000],
  searchKeyword: '',
  bookingVisible: false,
  selectedCourse: null
})

const loadCourses = () => {
  request.get('/course/selectAll').then(res => {
    if (res.code === '200') {
      data.courses = res.data.filter(course => course.status === 'ACTIVE')
      data.filteredCourses = [...data.courses]
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadCategories = () => {
  request.get('/category/selectAll').then(res => {
    if (res.code === '200') {
      data.categories = res.data
    }
  })
}

const filterCourses = () => {
  let filtered = [...data.courses]
  
  // 分类筛选
  if (data.filterCategory) {
    filtered = filtered.filter(course => course.categoryId === data.filterCategory)
  }
  
  // 价格筛选
  filtered = filtered.filter(course => 
    course.price >= data.priceRange[0] && course.price <= data.priceRange[1]
  )
  
  // 关键词搜索
  if (data.searchKeyword) {
    filtered = filtered.filter(course => 
      course.name.toLowerCase().includes(data.searchKeyword.toLowerCase())
    )
  }
  
  data.filteredCourses = filtered
}

const resetFilters = () => {
  data.filterCategory = ''
  data.priceRange = [0, 1000]
  data.searchKeyword = ''
  data.filteredCourses = [...data.courses]
}

const bookCourse = (course) => {
  if (!data.user.id) {
    ElMessage.warning('请先登录')
    return
  }
  
  if (!data.user.email) {
    ElMessage.warning('请先设置邮箱地址，以便接收预约通知')
    return
  }
  
  data.selectedCourse = course
  data.bookingVisible = true
}

const confirmBooking = () => {
  const bookingData = {
    userId: data.user.id,
    courseId: data.selectedCourse.id,
    courseType: 'COURSE',
    title: data.selectedCourse.name,
    content: `预约课程：${data.selectedCourse.name}，教练：${data.selectedCourse.coachName}`
  }
  
  request.post('/apply/add', bookingData).then(res => {
    if (res.code === '200') {
      ElMessage.success('预约申请提交成功，请等待管理员审核')
      data.bookingVisible = false
      data.selectedCourse = null
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const logout = () => {
  localStorage.removeItem('quan_user')
  router.push('/login')
}

onMounted(() => {
  loadCourses()
  loadCategories()
})
</script>

<style scoped>
.course-container {
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

.filter-section {
  background: rgba(255, 255, 255, 0.95);
  padding: 20px 0;
  backdrop-filter: blur(10px);
}

.filter-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.price-label {
  text-align: center;
  font-size: 12px;
  color: #666;
  margin-top: 5px;
}

.course-content {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
}

.course-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15);
}

.course-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.course-status.active {
  background: #67c23a;
  color: white;
}

.course-status.disabled {
  background: #f56c6c;
  color: white;
}

.course-info {
  padding: 24px;
}

.course-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 12px 0;
}

.course-description {
  color: #5a6c7d;
  line-height: 1.6;
  margin: 0 0 20px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.course-details {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  font-size: 14px;
}

.course-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f2f5;
}

.course-price {
  font-size: 1.5rem;
  font-weight: 700;
  color: #e74c3c;
}

.el-empty {
  padding: 60px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }
  
  .course-grid {
    grid-template-columns: 1fr;
  }
  
  .filter-content .el-row {
    flex-direction: column;
    gap: 16px;
  }
  
  .filter-content .el-col {
    width: 100% !important;
  }
}
</style>