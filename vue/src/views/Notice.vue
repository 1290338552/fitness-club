<template>
  <div>
    <div class="card"  >
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button clearable type="warning" @click="reset">重 置</el-button>
    </div>

    <div class="card">
      <el-button @click="handleAdd" type="primary" v-if="data.user.role === 'ADMIN'">新增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">

        <el-table-column prop="title" label="健身标题" width="180" />
        <el-table-column prop="content" label="训练内容" >
          <template v-slot="scope">
            <span v-if="data.user.role === 'ADMIN'">{{scope.row.content}}</span>
            <span v-else>站无权限</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" />

        <el-table-column label="操作" v-if="data.user.role === 'ADMIN'">
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

    <el-dialog title="健身公告信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form  ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">

        <el-form-item prop="title" label="健身标题" >
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入健身标题"/>
        </el-form-item>
        <el-form-item prop="content" label="训练内容" >
          <el-input type="textarea" rows="3" v-model="data.form.content" autocomplete="off" placeholder="请输入训练内容"/>
        </el-form-item>
<!--        <el-form-item prop="time" label="发布时间" >-->
<!--          <el-input v-model="data.form.time" autocomplete="off" placeholder="请输入发布时间"/>-->
<!--        </el-form-item>-->

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

import router from "@/router/index.js";
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data  = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  title:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  rules:{
    title: [
      { required: true, message: '请输入健身标题', trigger: 'blur' },
      { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请输入训练内容', trigger: 'blur' },
      { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
    ]
  }
})

const load = () => {
  request.get("/notice/selectPage",{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title
    }
  }).then(res => {
    if (res.code === "200"){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深拷贝给表单
  data.formVisible = true//回显
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复','请确认删除',{type: 'warning'}).then(res => {
    request.delete("/notice/delete/"+id).then(res => {
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
  request.post("/notice/add",data.form).then(res => {
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
  request.put("/notice/update",data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("新增成功")
      data.formVisible = false
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid){
      data.form.id ? update() : add() //通过id判断是修改还是新增，如果没有就新增有就修改
    }
  })
}

const reset = () => {
  data.title = null
  load()
}
</script>