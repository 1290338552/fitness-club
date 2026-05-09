<template>
  <div>
    <div class="card"  >
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button clearable type="warning" @click="reset">重 置</el-button>
    </div>
    <div class="card" v-if="data.user.role === 'USER'">
      <el-button @click="handleCourseAdd" type="primary" >课程预约</el-button>
      <el-button @click="handleAdd" type="success" >自定义课程</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">

        <el-table-column prop="title" label="课程标题"/>
        <el-table-column prop="content" label="训练内容"/>
        <el-table-column prop="courseName" label="预约课程">
          <template #default="scope">
            <span v-if="scope.row.courseType === 'COURSE'">{{ scope.row.courseName }}</span>
            <span v-else style="color: #999;">自定义课程</span>
          </template>
        </el-table-column>
        <el-table-column prop="coachName" label="教练"/>
        <el-table-column prop="coursePrice" label="价格" width="80">
          <template #default="scope">
            <span v-if="scope.row.coursePrice">¥{{ scope.row.coursePrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="申请人"/>
        <el-table-column prop="time" label="提交时间"/>
        <el-table-column prop="status" label="预约状态">
          <template v-slot="scope">
            <el-tag  v-if="scope.row.status === '预约中'" type="info">{{scope.row.status}}</el-tag>
            <el-tag  v-if="scope.row.status === '预约成功'" type="success">{{scope.row.status}}</el-tag>
            <el-tag  v-if="scope.row.status === '预约失败'" type="danger">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="预约失败说明"/>


        <el-table-column label="操作" width="100">
          <template #default="scope" v-if="data.user.role === 'USER'">
            <el-button :disabled="scope.row.status !== '预约中'" type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button :disabled="scope.row.status !== '预约中'" type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>

          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button :disabled="scope.row.status !== '预约中'" type="primary"  circle @click="handleEdit(scope.row)">审批</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-size="data.pageSize"
          layout="total,size, prev, pager, next,jumper"
          :page-sizes="[5, 10, 20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"

      />
    </div>

    <el-dialog :title="data.dialogTitle" v-model="data.formVisible" width="600" destroy-on-close>
      <el-form  ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 30px 20px 0">

        <!-- 课程选择 -->
        <el-form-item prop="courseId" label="选择课程" v-if="data.user.role === 'USER' && data.form.courseType === 'COURSE'">
          <el-select v-model="data.form.courseId" placeholder="请选择课程" style="width: 100%" @change="onCourseChange">
            <el-option v-for="course in data.courses" :key="course.id" :label="course.name" :value="course.id">
              <span style="float: left">{{ course.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">¥{{ course.price }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <!-- 课程信息展示 -->
        <div v-if="data.selectedCourse && data.form.courseType === 'COURSE'" style="margin-bottom: 20px;">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="课程名称">{{ data.selectedCourse.name }}</el-descriptions-item>
            <el-descriptions-item label="课程价格">¥{{ data.selectedCourse.price }}</el-descriptions-item>
            <el-descriptions-item label="课程时长">{{ data.selectedCourse.duration }}分钟</el-descriptions-item>
            <el-descriptions-item label="授课教练">{{ data.selectedCourse.coachName }}</el-descriptions-item>
            <el-descriptions-item label="课程描述" :span="2">{{ data.selectedCourse.description }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 自定义课程信息 -->
        <el-form-item prop="title" label="课程标题" v-if="data.user.role === 'USER' && data.form.courseType === 'CUSTOM'">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入课程标题"/>
        </el-form-item>
        <el-form-item prop="content" label="课程说明" v-if="data.user.role === 'USER' && data.form.courseType === 'CUSTOM'">
          <el-input type="textarea" rows="3" v-model="data.form.content" autocomplete="off" placeholder="请输入课程说明"/>
        </el-form-item>

        <el-form-item prop="status" label="预约状态"  v-if="data.user.role === 'ADMIN'">
          <!-- AI 建议区域 -->
          <div v-if="data.aiAdvice || data.aiLoading" style="width:100%;margin-bottom:12px;padding:10px 14px;background:#f3f0f8;border-radius:8px;border-left:3px solid #7F55B1">
            <div style="font-size:13px;color:#7F55B1;font-weight:bold;margin-bottom:4px">🤖 AI 审批建议</div>
            <div v-if="data.aiLoading" style="color:#999;font-size:13px">分析中...</div>
            <div v-else style="font-size:13px;color:#333;white-space:pre-line">{{ data.aiAdvice }}</div>
          </div>
          <el-radio-group v-model="data.form.status" >
            <el-radio-button  label="预约中" value="预约中" />
            <el-radio-button  label="预约成功" value="预约成功" />
            <el-radio-button  label="预约失败" value="预约失败" />
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="reason" label="预约失败说明"  v-if="data.user.role === 'ADMIN' && data.form.status === '预约失败'">
          <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入预约失败说明"/>
        </el-form-item>

      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>

import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()
const data  = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  title: null,
  tableData: [],
  formVisible: false,
  form: {},
  dialogTitle: '预约信息',
  courses: [], // 课程列表
  selectedCourse: null, // 选中的课程
  aiAdvice: '',
  aiLoading: false,
  rules: {
    title: [
      {required: true, message: '请输入标题', trigger: 'blur'},
      {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
    ],
    content: [
      {required: true, message: '请输入说明', trigger: 'blur'},
      {min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur'}
    ],
    courseId: [
      {required: true, message: '请选择课程', trigger: 'change'}
    ]
  }
})
const load = () => {
  request.get("/apply/selectPage",{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      // userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const handleAdd = () => {
  data.form = {
    courseType: 'CUSTOM'
  }
  data.form.userId = data.user.id
  data.dialogTitle = '自定义课程预约'
  data.selectedCourse = null
  data.formVisible = true
}

const handleCourseAdd = () => {
  loadCourses()
  data.form = {
    courseType: 'COURSE'
  }
  data.form.userId = data.user.id
  data.dialogTitle = '课程预约'
  data.selectedCourse = null
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.aiAdvice = ''
  data.formVisible = true
  // 管理员审批时请求 AI 建议
  if (data.user.role === 'ADMIN') {
    data.aiLoading = true
    request.get('/ai/applyAdvice/' + row.id).then(res => {
      if (res.code === '200') data.aiAdvice = res.data
    }).finally(() => { data.aiLoading = false })
  }
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复','请确认删除',{type: 'warning'}).then(res => {
    request.delete("/apply/delete/"+id).then(res => {
      if (res.code === '200'){
        ElMessage.success("删除成功")
        load()
      }else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const add = () =>{
  request.post("/apply/add",data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("新增成功")
      data.formVisible = false
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}
const update = () =>{
  request.put("/apply/update",data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("操作成功")
      data.formVisible = false
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.title = null
  load()
}
const loadCourses = () => {
  request.get("/course/selectAll").then(res => {
    if (res.code === '200') {
      data.courses = res.data
    }
  })
}

const onCourseChange = (courseId) => {
  data.selectedCourse = data.courses.find(course => course.id === courseId)
  if (data.selectedCourse) {
    data.form.title = data.selectedCourse.name
    data.form.content = data.selectedCourse.description
  }
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid){
      data.form.id ? update() : add() //通过id判断是修改还是新增，如果没有就新增有就修改
    }
  })
}
</script>