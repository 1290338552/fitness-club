
<template>
  <div>
    <div class="card">
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.username" placeholder="请输入账号" :prefix-icon="Search"></el-input>
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.name" placeholder="请输入名称查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button clearable type="warning" @click="reset">重 置</el-button>
    </div>
    <div class="card">
      <el-button @click="handleAdd" type="primary">新增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
      <el-button type="success" @click="exportData">批量导出</el-button>
      <el-upload
          style="display: inline-block;margin-left: 10px"
          action="http://localhost:9999/admin/import"
          :show-file-list="false"
          :on-success="handleImportSuccess"
      >
        <el-button type="info">批量导出</el-button>
      </el-upload>


    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                @selection-change="handleSelectionChange"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column label="头像">
          <template #default="scope">
            <el-image style="width: 40px;height: 40px;border-radius: 50%;display: block" v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list=[scope.row.avatar] :preview-teleported="true" ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="名称" width="180" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="email" label="邮箱" />

        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
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

    <el-dialog title="管理员信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">

        <el-form-item prop="username" label="账号" >
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="name" label="名称" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item prop="phone" label="电话" >
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱" >
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item prop="avatar" label="头像" >
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{token:data.user.token}"
              :on-success="handleUploadSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
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
import {reactive,ref} from "vue"
import {Search} from "@element-plus/icons-vue"
import axios from "axios";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  user:JSON.parse(localStorage.getItem('quan_user')  ||  '{}'),
  username:null,
  name:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  formVisible:false,
  form:{},
  rules:{
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' },
    ],
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ],
  },
  rows:[],
  ids:[]
})

const formRef = ref()

const load = () => {
  request.get("/admin/selectPage",{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      username:data.username,
      name:data.name
    }
  }).then(res => {
    if (res.code === '200'){
      data.tableData = res.data.list
      data.total = res.data.total
    }else {
      ElMessage.error(res.msg)
    }

  })
}
load()

const reset = () => {
  data.username = null
  data.name = null
  load()
}
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}
const add = () => {
  //formRef表单的引用
  formRef.value.validate((valid) => {
    if (valid) { //验证通过的情况下
      request.post("/admin/add",data.form).then(res => {
        if (res.code === '200'){
          ElMessage.success("新增成功")
          data.formVisible = false
          load()
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row) => {
  data.formVisible = true
  data.form = JSON.parse(JSON.stringify(row))  //深度拷贝数据
}

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) { //验证通过的情况下
      request.put("/admin/update",data.form).then(res => {
        if (res.code === '200'){
          ElMessage.success("修改成功")
          data.formVisible = false
          load()
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const save = () => {
  //根据id有就更新，没有就新增
  data.form.id ? update() : add()
}
const del = (id) => {
  ElMessageBox.confirm('此操作将永久删除该数据吗, 是否继续?', '删除确认', {type: 'warning'}).then(res => {
request.delete('/admin/delete/' + id).then(res => {
  if (res.code === '200'){
    ElMessage.success("删除成功")
    load()
  }else {
    ElMessage.error(res.msg)
  }
})
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {  //实际选择的数组
  data.rows = rows
  data.ids = data.rows.map(v => v.id) //可以把对象的数组转换成一个纯数字的数组
}

const deleteBatch = () => {
  if (data.rows.length === 0){
    ElMessage.warning("请选择要删除的数据")
    return
  }
  ElMessageBox.confirm('此操作将永久删除该数据吗, 是否继续?', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/admin/deleteBatch', {data:data.rows}).then(res => {
      if (res.code === '200'){
        ElMessage.success("批量删除成功")
        load()
      }else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const exportData = () => {
  let idsStr = data.ids.join(",") //把数组转换成字符串
  let url = `http://localhost:9999/admin/export?username=${data.username === null ? '' : data.username}`
   + `&name=${data.name === null ? '' : data.name}`
  + `&ids=${idsStr}`
  + `&token=${data.user.token}`
  window.open(url)
}

const handleImportSuccess =() => {
  ElMessage.success("导入成功")
  load()
}
const handleUploadSuccess = (res) => {
  data.form.avatar = res.data
}
</script>