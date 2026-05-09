<template>
  <div>
    <div class="card"  >
      <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.coachName" placeholder="请输入教练名字查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button clearable type="warning" @click="reset">重 置</el-button>
    </div>

    <div class="card">
      <el-button @click="handleAdd" type="primary" v-if="data.user.role === 'ADMIN'">新增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column prop="coachImg" label="头像">
          <template #default="scope">
            <el-image style="width: 40px;height: 40px;border-radius: 5px;display: block" v-if="scope.row.coachImg" :src="scope.row.coachImg" :preview-src-list=[scope.row.coachImg] :preview-teleported="true" ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="发布会员" width="180" />
        <el-table-column prop="coachName" label="教练名字" />
        <el-table-column prop="time" label="发布时间" />
        <el-table-column prop="status" label="预约状态">
          <template v-slot="scope">
            <el-tag  v-if="scope.row.status === '预约中'" type="info">{{scope.row.status}}</el-tag>
            <el-tag  v-if="scope.row.status === '预约成功'" type="success">{{scope.row.status}}</el-tag>
            <el-tag  v-if="scope.row.status === '预约失败'" type="danger">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="拒接说明" />

        <el-table-column label="操作" width="200px" v-if="data.user.role === 'ADMIN'">
          <template #default="scope">
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
    <el-dialog title="预约审核信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form  ref="formRef" :model="data.form"  label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="status" label="预约状态"  v-if="data.user.role === 'ADMIN'">
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

import router from "@/router/index.js";
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data  = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  coachName:null,
  pageNum:1,
  pageSize:5,
  total:0,
  tableData:[],
  form:{},
  formVisible:false,
})

const load = () => {
  request.get("/record/selectPage",{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      coachName:data.coachName
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


const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深拷贝给表单
  data.formVisible = true//回显
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复','请确认删除',{type: 'warning'}).then(res => {
    request.delete("/record/delete/"+id).then(res => {
      if (res.code === '200'){
        ElMessage.success("删除成功")
        load()
      }else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}


const update = () =>{
  request.put("/record/update",data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success("预约成功")
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
      update()
    }
  })
}

const reset = () => {
  data.coachName = null
  load()
}
</script>