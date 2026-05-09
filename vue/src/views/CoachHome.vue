<template>
  <div style="padding: 20px">
    <!-- 欢迎卡片 -->
    <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);color: white;padding: 30px;border-radius: 10px;margin-bottom: 20px">
      <div style="font-size: 24px;font-weight: bold;margin-bottom: 8px">欢迎回来，{{ data.user.name }} 教练！</div>
      <div style="font-size: 14px;opacity: 0.9">今天也要元气满满地指导学员哦 💪</div>
    </div>

    <!-- 统计卡片 -->
    <div style="display: grid;grid-template-columns: repeat(3, 1fr);gap: 20px;margin-bottom: 20px">
      <div class="stat-card">
        <div style="font-size: 36px;font-weight: bold;color: #409eff">{{ data.courseCount }}</div>
        <div style="color: #999;margin-top: 5px">我的课程数</div>
      </div>
      <div class="stat-card">
        <div style="font-size: 36px;font-weight: bold;color: #67c23a">{{ data.applyCount }}</div>
        <div style="color: #999;margin-top: 5px">课程预约数</div>
      </div>
      <div class="stat-card">
        <div style="font-size: 36px;font-weight: bold;color: #e6a23c">{{ data.recordCount }}</div>
        <div style="color: #999;margin-top: 5px">教练预约数</div>
      </div>
    </div>

    <!-- 最近预约 -->
    <div class="card">
      <div style="font-size: 16px;font-weight: bold;margin-bottom: 15px">最近课程预约</div>
      <el-table :data="data.recentApply" style="width: 100%">
        <el-table-column prop="userName" label="学员" />
        <el-table-column prop="title" label="课程" />
        <el-table-column prop="time" label="预约时间" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === '预约成功' ? 'success' : scope.row.status === '预约中' ? 'warning' : 'danger'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import request from '@/utils/request.js'

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  courseCount: 0,
  applyCount: 0,
  recordCount: 0,
  recentApply: []
})

const loadData = () => {
  // 加载我的课程数
  request.get('/course/selectByCoachId/' + data.user.id).then(res => {
    if (res.code === '200') data.courseCount = res.data?.length || 0
  })

  // 加载课程预约
  request.get('/apply/selectPage', { params: { pageNum: 1, pageSize: 5 } }).then(res => {
    if (res.code === '200') {
      data.recentApply = res.data?.list || []
      data.applyCount = res.data?.total || 0
    }
  })

  // 加载教练预约数
  request.get('/record/selectPage', { params: { pageNum: 1, pageSize: 5, coachId: data.user.id } }).then(res => {
    if (res.code === '200') data.recordCount = res.data?.total || 0
  })
}

onMounted(() => loadData())
</script>

<style scoped>
.stat-card {
  background: white;
  padding: 25px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
</style>
