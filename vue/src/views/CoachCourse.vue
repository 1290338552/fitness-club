<template>
  <div>
    <!-- 我的课程 -->
    <div class="card">
      <div style="font-size: 16px;font-weight: bold;margin-bottom: 15px">我的课程</div>
      <el-table :data="data.courses" style="width: 100%" :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column prop="image" label="图片" width="80">
          <template #default="scope">
            <el-image v-if="scope.row.image" :src="scope.row.image" style="width:50px;height:50px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称" />
        <el-table-column prop="description" label="课程描述" show-overflow-tooltip />
        <el-table-column prop="price" label="价格" width="90">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" size="small" @click="showApply(scope.row)">查看预约</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 预约信息 -->
    <div class="card" v-if="data.currentCourse">
      <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:15px">
        <div style="font-size:16px;font-weight:bold">
          「{{ data.currentCourse.name }}」的预约信息
        </div>
        <el-input v-model="data.searchName" placeholder="搜索学员姓名" clearable
                  style="width:200px" @clear="loadApply" @keyup.enter="loadApply">
          <template #append>
            <el-button @click="loadApply">搜索</el-button>
          </template>
        </el-input>
      </div>

      <el-table :data="data.applyList" style="width: 100%" :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column prop="userName" label="学员姓名" />
        <el-table-column prop="time" label="预约时间" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '预约成功' ? 'success' : scope.row.status === '预约中' ? 'warning' : 'danger'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="备注" show-overflow-tooltip />
      </el-table>

      <div style="margin-top:15px">
        <el-pagination
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            layout="total, prev, pager, next"
            :total="data.total"
            @current-change="loadApply"
        />
      </div>
    </div>

    <!-- 未选择课程时的提示 -->
    <div class="card" v-else style="text-align:center;color:#999;padding:40px">
      <el-icon :size="50" style="margin-bottom:10px"><List /></el-icon>
      <div>点击课程的「查看预约」按钮，查看该课程的预约信息</div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { List } from '@element-plus/icons-vue'
import request from '@/utils/request.js'

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  courses: [],
  currentCourse: null,
  applyList: [],
  searchName: '',
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 加载当前教练的课程
const loadCourses = () => {
  console.log('当前教练用户:', data.user)
  console.log('教练ID:', data.user.id)
  request.get('/course/selectByCoachId/' + data.user.id).then(res => {
    console.log('课程列表响应:', res)
    if (res.code === '200') data.courses = res.data || []
  })
}

// 点击查看预约
const showApply = (course) => {
  data.currentCourse = course
  data.pageNum = 1
  data.searchName = ''
  loadApply()
}

// 加载该课程的预约信息
const loadApply = () => {
  const params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    courseId: data.currentCourse.id
  }
  if (data.searchName) params.userName = data.searchName
  console.log('查询预约参数:', params)
  request.get('/apply/selectPage', { params }).then(res => {
    console.log('预约列表响应:', res)
    if (res.code === '200') {
      data.applyList = res.data?.list || []
      data.total = res.data?.total || 0
    }
  })
}

onMounted(() => loadCourses())
</script>
