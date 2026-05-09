<template>
  <div style="background-color: #faf6f6; min-height: 1000px">
    <el-menu
        :default-active="$route.path"
        class="el-menu-demo"
        mode="horizontal"
        :router="true"
    >
      <el-menu-item index="/front/home">系统首页</el-menu-item>
      <el-menu-item index="/front/course">精品课程</el-menu-item>
      <el-menu-item index="/front/home/frontnotice">公告</el-menu-item>
      <el-menu-item @click="logout">退出</el-menu-item>
    </el-menu>

    <div style="margin-bottom: 20px">
      <el-carousel trigger="click" height="350px">
        <el-carousel-item v-for="item in data.introductionData" :key="item.id">
          <img :src="item.img" alt="" style="height: 300px;width: 100%">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="width: 80%;margin: 20px auto">
      <div style="font-size: 18px;font-weight: bold;border-left: 5px solid pink;padding-left: 5px;margin-bottom: 10px">健身攻略</div>
      <div style="margin-top: 20px;display: flex;grid-gap: 20px" v-for="item in data.introductionData" :key="item.id">
        <div style="flex: 1;">
          <img @click="navToDetail(item.id)" :src="item.img" alt="" style="width: 100%; height:230px;display: block;border-radius: 5px;cursor: pointer">
        </div>
        <div style="flex: 3;">
          <div style="font-size: 20px;font-weight: bold;cursor: pointer" @click="navToDetail(item.id)">{{item.title}}</div>
          <div class="line5" style="margin-top: 10px;font-size: 15px;color: #666666;line-height: 25px;height: 125px;text-align: justify">{{item.description}}</div>
          <div style="display: flex; align-items: center; margin-top: 10px;grid-gap: 10px" >
            <img :src="item.userAvatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
            <div style="font-size: 14px;">{{item.userName}}</div>
            <div style="font-size: 14px; color: #666666">{{item.time}}</div>
          </div>
        </div>
      </div>
    </div>

    <div style="width: 80%;margin: 20px auto">
      <div style="font-size: 18px;font-weight: bold;border-left: 5px solid pink;padding-left: 5px;margin-bottom: 10px">健身攻略</div>
      <div>
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in data.introductionData" :key="item.id" style="margin-bottom: 20px">
            <img @click="navToDetail(item.id)" :src="item.img" alt="" style="width: 100%; height:244px; border-radius: 5px; cursor: pointer">
            <div style="font-size: 16px;font-weight: bold;margin-top: 10px; cursor: pointer" @click="navToDetail(item.id)">{{item.title}}</div>
            <div style="display: flex; align-items: center; margin-top: 10px;grid-gap: 10px" >
              <img :src="item.userAvatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
              <div style="font-size: 14px;">{{item.userName}}</div>
              <div style="font-size: 14px; color: #666666">{{item.time}}</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <AiChat />
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { useRouter } from 'vue-router';
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import AiChat from '@/components/AiChat.vue'

const router = useRouter();

const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  introductionData: [],
  carouselData: []
})

const loadIntroduction = () => {
  request.get('/introduction/selectAll').then(res => {
    if (res.code === '200'){
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadIntroduction()

const logout = () => {
  localStorage.removeItem('quan_user')
  // 使用路由跳转到登录页
  router.push('/login')
}

// 使用路由跳转到详情页
const navToDetail = (id) => {
  router.push({
    path: '/front/introductionDetail',
    query: { id: id }
  })
}
</script>

<style>
.line5 {
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
  overflow: hidden;
}

.home-container {
  background-color: #faf6f6;
  min-height: 1000px;
}
</style>