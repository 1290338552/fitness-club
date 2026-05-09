<template>
  <div class="coach-workspace">
    <!-- 顶部导航栏 -->
    <div class="header">
      <div class="header-left">
        <h2>教练工作台</h2>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :src="data.user.avatar" v-if="data.user.avatar" />
            <el-avatar v-else>{{ data.user.name?.charAt(0) }}</el-avatar>
            <span style="margin-left: 10px">{{ data.user.name }}</span>
            <el-icon style="margin-left: 5px"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 欢迎卡片 -->
      <div class="welcome-card">
        <h1>欢迎回来，{{ data.user.name }}教练！</h1>
        <p>今天也要元气满满地指导学员哦~ 💪</p>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <el-icon :size="30"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ data.stats.totalStudents }}</div>
            <div class="stat-label">预约学员</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon" style="background: #67c23a">
            <el-icon :size="30"><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ data.stats.todayCourses }}</div>
            <div class="stat-label">今日课程</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <el-icon :size="30"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ data.stats.totalCourses }}</div>
            <div class="stat-label">总课程数</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <el-icon :size="30"><Star /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ data.stats.rating }}</div>
            <div class="stat-label">学员评分</div>
          </div>
        </div>
      </div>

      <!-- 我的课程 -->
      <div class="section-card">
        <div class="section-header">
          <h3>我的课程</h3>
          <el-button type="primary" size="small">查看全部</el-button>
        </div>
        <el-table :data="data.courses" style="width: 100%">
          <el-table-column prop="name" label="课程名称" />
          <el-table-column prop="description" label="课程描述" show-overflow-tooltip />
          <el-table-column prop="price" label="价格" width="100">
            <template #default="scope">
              ¥{{ scope.row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="duration" label="时长(分钟)" width="100" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 最近预约 -->
      <div class="section-card">
        <div class="section-header">
          <h3>最近预约</h3>
          <el-button type="primary" size="small">查看全部</el-button>
        </div>
        <el-table :data="data.records" style="width: 100%">
          <el-table-column prop="userName" label="学员姓名" />
          <el-table-column prop="userPhone" label="联系电话" />
          <el-table-column prop="time" label="预约时间" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.status === '预约成功'" type="success">预约成功</el-tag>
              <el-tag v-else-if="scope.row.status === '预约中'" type="warning">预约中</el-tag>
              <el-tag v-else type="danger">预约失败</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 个人信息对话框 -->
    <el-dialog title="个人信息" v-model="data.profileVisible" width="500px">
      <el-form :model="data.user" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="data.user.name" disabled />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="data.user.username" disabled />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="data.user.phone" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.user.email" disabled />
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog title="修改密码" v-model="data.passwordVisible" width="400px">
      <el-form :model="data.passwordForm" label-width="100px">
        <el-form-item label="新密码">
          <el-input type="password" v-model="data.passwordForm.newPassword" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input type="password" v-model="data.passwordForm.confirmPassword" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="data.passwordVisible = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown, User, Calendar, TrendCharts, Star } from '@element-plus/icons-vue'
import request from '@/utils/request.js'

const router = useRouter()

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  profileVisible: false,
  passwordVisible: false,
  passwordForm: {
    newPassword: '',
    confirmPassword: ''
  },
  stats: {
    totalStudents: 0,
    todayCourses: 0,
    totalCourses: 0,
    rating: 5.0
  },
  courses: [],
  records: []
})

// 加载教练数据
const loadData = () => {
  // 加载我的课程
  request.get('/course/selectByCoachId/' + data.user.id).then(res => {
    if (res.code === '200') {
      data.courses = res.data || []
      data.stats.totalCourses = data.courses.length
    }
  })

  // 加载预约记录
  request.get('/record/selectAll', {
    params: { coachId: data.user.id }
  }).then(res => {
    if (res.code === '200') {
      data.records = res.data || []
      data.stats.totalStudents = data.records.filter(r => r.status === '预约成功').length
    }
  })
}

// 下拉菜单命令处理
const handleCommand = (command) => {
  if (command === 'profile') {
    data.profileVisible = true
  } else if (command === 'password') {
    data.passwordVisible = true
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    }).then(() => {
      localStorage.removeItem('quan_user')
      localStorage.removeItem('token')
      router.push('/login')
      ElMessage.success('退出成功')
    })
  }
}

// 修改密码
const updatePassword = () => {
  if (!data.passwordForm.newPassword) {
    ElMessage.warning('请输入新密码')
    return
  }
  if (data.passwordForm.newPassword !== data.passwordForm.confirmPassword) {
    ElMessage.warning('两次密码输入不一致')
    return
  }

  request.post('/updatePassword', {
    id: data.user.id,
    password: data.passwordForm.newPassword,
    role: 'COACH'
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('密码修改成功，请重新登录')
      data.passwordVisible = false
      setTimeout(() => {
        localStorage.removeItem('quan_user')
        localStorage.removeItem('token')
        router.push('/login')
      }, 1500)
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.coach-workspace {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: white;
  padding: 0 30px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.header-left h2 {
  margin: 0;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.main-content {
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px;
  border-radius: 10px;
  margin-bottom: 30px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

.welcome-card h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
}

.welcome-card p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 25px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.section-card {
  background: white;
  padding: 25px;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  font-size: 20px;
  color: #333;
}
</style>
