<template>
  <div style="width: 70%;margin: 20px auto">
    <div style="text-align: center;font-size: 20px;font-weight: bold">{{data.introductionData.title}}</div>
    <div style="margin-top: 20px;display: flex;align-items: center;justify-content: center;grid-gap: 20px">
      <img :src="data.introductionData.userAvatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
      <div style="font-size: 16px;font-weight: bold;margin-bottom: 10px">作者：{{data.introductionData.userName}}</div>
      <div style="font-size: 16px;font-weight: bold;margin-bottom: 10px">所属分类：{{data.introductionData.categoryTitle}}</div>
      <div style="font-size: 16px;font-weight: bold;margin-bottom: 10px">发布时间：{{data.introductionData.time}}</div>

<!--      <div style="font-size: 16px;font-weight: bold;margin-bottom: 10px">浏览次数：{{data.introductionData.view}}</div>-->
    </div>
    <div v-html="data.introductionData.content" style="margin-top: 50px;padding: 0 50px"></div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";


const data  = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  introductionId: router.currentRoute.value.query.id,
  introductionData: {}
})

const loadIntroduction = () => {
  // console.log(data.introductionId)
  request.get('/introduction/selectById/' + data.introductionId).then(res => {
    if (res.code === '200'){
      data.introductionData = res.data
    }else {
      ElMessage.error(res.msg)
    }
  })
}

loadIntroduction()
</script>