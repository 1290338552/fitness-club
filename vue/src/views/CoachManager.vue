<template>
  <div>
    <!-- 头部区域 -->
    <div style="height: 60px;display: flex;">
      <div style="width: 240px;display: flex;align-items: center;padding-left: 20px;background-color: #574964;">
        <img style="width: 40px;height: 40px;border-radius: 50%" src="@/assets/imgs/1750395292768_tutou.png">
        <span style="font-size: 18px;font-weight: bold;color: #f1f1f1;margin-left: 5px">教练工作台</span>
      </div>
      <div style="flex: 1;display:flex;margin-left: 20px;align-items: center;border-bottom: 1px solid #ddd">
        <span style="margin-right: 5px;cursor: pointer" @click="router.push('/coachManager/home')">首页</span> /
        <span style="margin-left: 5px;">{{ router.currentRoute.value.meta.name }}</span>
      </div>
      <div style="width: fit-content;padding-right: 20px;display: flex;align-items: center;border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex;align-items: center;">
            <img v-if="data.user?.avatar" style="width: 40px;height: 40px;border-radius: 50%" :src="data.user?.avatar" />
            <img v-else style="width: 40px;height: 40px;border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png">
            <span style="margin-left: 5px">{{ data.user?.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/coachManager/info')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/coachManager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 下方区域 -->
    <div style="display: flex">
      <!-- 菜单区域 -->
      <div style="width: 240px;">
        <el-menu router :default-openeds="['1','2','3']" :default-active="router.currentRoute.value.path"
                 style="min-height: calc(100vh - 60px)">

          <el-menu-item index="/coachManager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>

          <el-sub-menu index="1">
            <template #title>
              <el-icon><Bell /></el-icon>
              <span>公告信息</span>
            </template>
            <el-menu-item index="/coachManager/notice">健身公告</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2">
            <template #title>
              <el-icon><List /></el-icon>
              <span>课程管理</span>
            </template>
            <el-menu-item index="/coachManager/course">我的课程</el-menu-item>
          </el-sub-menu>



        </el-menu>
      </div>

      <!-- 数据渲染区 -->
      <div style="flex: 1;width: 0;background-color: #FFE1E0">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js"
import { reactive } from "vue"
import { House, Bell, List } from '@element-plus/icons-vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}')
})

const logout = () => {
  localStorage.removeItem('quan_user')
  localStorage.removeItem('token')
  location.href = '/login'
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('quan_user') || '{}')
}
</script>

<style>
.el-menu {
  background-color: #7F55B1;
  border: none;
}
.el-sub-menu__title {
  color: #ddd;
}
.el-menu-item {
  height: 50px;
  color: #ddd;
}
.el-menu .is-active {
  background-color: #9B7EBD;
  color: #fff;
}
.el-sub-menu__title:hover {
  background-color: #F49BAB;
}
.el-menu-item:not(.is-active):hover {
  background-color: #F49BAB;
  color: #333;
}
</style>
