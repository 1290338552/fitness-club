<template>
  <div class="card" style="width: 50%">
    <div>个人 信息</div>

    <el-form  ref="formRef" :model="data.user"  label-width="80px" style="padding: 20px 30px 20px 0">

      <el-form-item prop="username" label="账号" >
        <el-input v-model="data.user.username" autocomplete="off" placeholder="请输入账号"/>
      </el-form-item>
      <el-form-item prop="name" label="名称" >
        <el-input v-model="data.user.name" autocomplete="off" placeholder="请输入名称"/>
      </el-form-item>
      <el-form-item prop="phone" label="电话" >
        <el-input v-model="data.user.phone" autocomplete="off" placeholder="请输入电话"/>
      </el-form-item>
      <el-form-item prop="email" label="邮箱" >
        <el-input v-model="data.user.email" autocomplete="off" placeholder="请输入邮箱"/>
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
    <div style="text-align: center">
      <el-button type="primary" style="padding: 20px 40px" @click="update">保存</el-button>
    </div>

  </div>
</template>
<script setup>
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";

const data = reactive({
  user:JSON.parse(localStorage.getItem('quan_user')  ||  '{}'),

})

const  handleUploadSuccess = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])

const update = () => {
  let url
  if (data.user.role === 'ADMIN'){
    url = '/admin/update'
  }
  if (data.user.role === 'USER'){
    url = '/user/update'
  }
  request.put(url,data.user).then(res => {
    if (res.code === '200'){
      ElMessage.success("修改成功")
      localStorage.setItem('quan_user',JSON.stringify(data.user))
      emit('updateUser',data.user)
    }
  })
}
</script>