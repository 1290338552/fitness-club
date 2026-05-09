<template>
  <div>
    <div class="card"  >
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.coach" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button clearable type="warning" @click="reset">重 置</el-button>
    </div>

    <div class="card">
      <el-button @click="handleAdd" type="primary" v-if="data.user.role === 'ADMIN'">新增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">

        <el-table-column prop="img" label="教练图片" width="180" >
          <template #default="scope">
            <el-image style="width: 40px;height: 40px;border-radius: 5px;display: block" v-if="scope.row.img" :src="scope.row.img" :preview-src-list=[scope.row.img] :preview-teleported="true" ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="教练职位"/>
        <el-table-column prop="price" label="教练价格"/>
        <el-table-column prop="coach" label="教练名字"/>
        <el-table-column prop="num" label="剩余名额" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.num > 0 ? 'success' : 'danger'">
              {{ scope.row.num > 0 ? scope.row.num + ' 人' : '已约满' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" >
          <template #default="scope" v-if="data.user.role === 'ADMIN'">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
          <template #default="scope" v-else>
            <el-button type="primary" :disabled="scope.row.num <= 0" @click="reservation(scope.row)">
              {{ scope.row.num > 0 ? '预约' : '已约满' }}
            </el-button>
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

    <el-dialog title="教练信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form  ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">

        <el-form-item prop="img" label="教练照片" >
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{token:data.user.token}"
              :on-success="handleUploadSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="教练职位" >
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入教练职位"/>
        </el-form-item>
        <el-form-item prop="price" label="教练价格" >
          <el-input   v-model="data.form.price" autocomplete="off" placeholder="请输入教练价格"/>
        </el-form-item>
        <el-form-item prop="coach" label="教练名字" >
          <el-input   v-model="data.form.coach" autocomplete="off" placeholder="请输入教练名字"/>
        </el-form-item>
        <el-form-item prop="num" label="剩余名额" v-if="data.user.role === 'ADMIN'">
          <el-input-number v-model="data.form.num" :min="0" :max="999" placeholder="请输入剩余名额" />
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

import router from "@/router/index.js";
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data  = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  coach:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
  rules:{
    img: [
      {required: true, message: '请上传图片', trigger: 'blur'}
    ],
    name: [
        {required: true, message: '请输入职位', trigger: 'blur'}
    ],
    price: [
        {required: true, message: '请输入价格', trigger: 'blur'}
    ],
    coach: [
        {required: true, message: '请输入名字', trigger: 'blur'}
    ],
    num: [
        {required: true, message: '请输入剩余名额', trigger: 'blur'}
    ]
  }
})

const load = () => {
  request.get("/coach/selectPage",{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      coach:data.coach
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
    request.delete("/coach/delete/"+id).then(res => {
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
  request.post("/coach/add",data.form).then(res => {
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
  request.put("/coach/update",data.form).then(res => {
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
  data.coach = null
  load()
}

const handleUploadSuccess = (res) => {
  data.form.img = res.data
}

const reservation = (row) => {
  request.post("/record/add",{
    userId:data.user.id,
    coachId:row.id,

  }).then(res => {
    if (res.code === '200'){
      ElMessage.success("操作成功，等待回复")
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}
</script>