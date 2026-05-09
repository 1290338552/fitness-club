<template>
  <div>
    <div class="card">
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.name" placeholder="请输入课程名称查询" :prefix-icon="Search"></el-input>
      <el-select v-model="data.status" placeholder="请选择状态" clearable style="width: 150px;margin-right: 5px">
        <el-option label="启用" value="ACTIVE"></el-option>
        <el-option label="禁用" value="DISABLED"></el-option>
      </el-select>
      <el-select v-model="data.categoryId" placeholder="请选择分类" clearable style="width: 150px;margin-right: 5px">
        <el-option v-for="item in data.categories" :key="item.id" :label="item.title" :value="item.id"></el-option>
      </el-select>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>
    
    <div class="card" v-if="data.user.role === 'ADMIN'">
      <el-button @click="handleAdd" type="primary">新增课程</el-button>
      <el-button @click="delBatch" type="danger">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column type="selection" width="55" v-if="data.user.role === 'ADMIN'"/>
        <el-table-column prop="image" label="课程图片" width="100">
          <template #default="scope">
            <el-image v-if="scope.row.image" :src="scope.row.image" style="width: 60px; height: 60px" fit="cover"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="课程名称"/>
        <el-table-column prop="description" label="课程描述" show-overflow-tooltip/>
        <el-table-column prop="price" label="价格(元)" width="100">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="duration" label="时长(分钟)" width="100"/>
        <el-table-column prop="categoryName" label="课程分类"/>
        <el-table-column prop="coachName" label="教练"/>
        <el-table-column prop="maxParticipants" label="最大人数" width="100"/>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" v-if="data.user.role === 'ADMIN'">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
            <el-button v-if="scope.row.status === 'ACTIVE'" type="warning" @click="updateStatus(scope.row.id, 'DISABLED')">禁用</el-button>
            <el-button v-else type="success" @click="updateStatus(scope.row.id, 'ACTIVE')">启用</el-button>
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

    <!-- 课程信息弹窗 -->
    <el-dialog :title="data.form.id ? '编辑课程' : '新增课程'" v-model="data.formVisible" width="600" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="name" label="课程名称">
          <el-input v-model="data.form.name" placeholder="请输入课程名称"/>
        </el-form-item>
        <el-form-item prop="description" label="课程描述">
          <el-input type="textarea" rows="3" v-model="data.form.description" placeholder="请输入课程描述"/>
        </el-form-item>
        <el-form-item prop="price" label="课程价格">
          <el-input-number v-model="data.form.price" :precision="2" :step="0.01" :min="0" placeholder="请输入价格"/>
        </el-form-item>
        <el-form-item prop="duration" label="课程时长">
          <el-input-number v-model="data.form.duration" :min="1" placeholder="请输入时长(分钟)"/>
        </el-form-item>
        <el-form-item prop="categoryId" label="课程分类">
          <el-select v-model="data.form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in data.categories" :key="item.id" :label="item.title" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="coachId" label="授课教练">
          <el-select v-model="data.form.coachId" placeholder="请选择教练" style="width: 100%">
            <el-option v-for="item in data.coaches" :key="item.id" :label="item.coach" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="maxParticipants" label="最大人数">
          <el-input-number v-model="data.form.maxParticipants" :min="1" placeholder="请输入最大参与人数"/>
        </el-form-item>
        <el-form-item prop="image" label="课程图片">
          <el-upload
              class="avatar-uploader"
              :action="data.server + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :on-error="handleAvatarError"
              :before-upload="beforeAvatarUpload">
            <img v-if="data.form.image" :src="data.form.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <div v-if="data.form.image" style="margin-top: 10px; color: #666; font-size: 12px;">
            当前图片: {{ data.form.image }}
          </div>
        </el-form-item>
        <el-form-item prop="status" label="课程状态">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="ACTIVE">启用</el-radio-button>
            <el-radio-button label="DISABLED">禁用</el-radio-button>
          </el-radio-group>
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
import {Search, Plus} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  server: 'http://localhost:9999',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  status: null,
  categoryId: null,
  tableData: [],
  formVisible: false,
  form: {},
  ids: [],
  categories: [],
  coaches: [],
  rules: {
    name: [
      {required: true, message: '请输入课程名称', trigger: 'blur'},
      {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
    ],
    description: [
      {required: true, message: '请输入课程描述', trigger: 'blur'}
    ],
    price: [
      {required: true, message: '请输入课程价格', trigger: 'blur'}
    ],
    duration: [
      {required: true, message: '请输入课程时长', trigger: 'blur'}
    ],
    categoryId: [
      {required: true, message: '请选择课程分类', trigger: 'change'}
    ],
    coachId: [
      {required: true, message: '请选择授课教练', trigger: 'change'}
    ],
    maxParticipants: [
      {required: true, message: '请输入最大参与人数', trigger: 'blur'}
    ]
  }
})

const load = () => {
  request.get("/course/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      status: data.status,
      categoryId: data.categoryId
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadCategories = () => {
  request.get("/category/selectAll").then(res => {
    if (res.code === '200') {
      data.categories = res.data
    }
  })
}

const loadCoaches = () => {
  request.get("/coach/selectAll").then(res => {
    if (res.code === '200') {
      data.coaches = res.data
    }
  })
}

load()
loadCategories()
loadCoaches()

const handleAdd = () => {
  data.form = {
    status: 'ACTIVE',
    maxParticipants: 1
  }
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复', '请确认删除', {type: 'warning'}).then(res => {
    request.delete("/course/delete/" + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复', '请确认删除', {type: 'warning'}).then(res => {
    request.delete("/course/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleSelectionChange = (val) => {
  data.ids = val.map(v => v.id)
}

const add = () => {
  request.post("/course/add", data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success("新增成功")
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put("/course/update", data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success("修改成功")
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const updateStatus = (id, status) => {
  request.put("/course/updateStatus", null, {
    params: {id, status}
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功")
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.name = null
  data.status = null
  data.categoryId = null
  load()
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const handleAvatarSuccess = (res, file) => {
  console.log('上传成功响应:', res)
  console.log('文件信息:', file)
  
  if (res.code === '200') {
    data.form.image = res.data
    console.log('设置图片URL:', res.data)
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败: ' + res.msg)
  }
}

const handleAvatarError = (err, file, fileList) => {
  console.error('上传失败:', err)
  ElMessage.error('图片上传失败，请重试')
}

const beforeAvatarUpload = (file) => {
  console.log('准备上传文件:', file)
  
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader .el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>