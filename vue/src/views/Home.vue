<template>
  <div>
    <div class="card">
      <div style="flex: 1;text-align: center;font-size: 24px;background-color: #9B7EBD; display: flex;justify-content:center">
        欢迎<span style="color: rgb(0, 255, 128);">{{data.user?.name}}</span>来到体育俱乐部信息管理系统
      </div>
    </div>
    <div class="card" style="margin-top: 10px;width: 50%" v-if="data.user.role === 'USER'">
      <div style="font-size: 18px;margin-bottom: 20px">健身公告</div>
      <div class="demo-collapse">
        <el-collapse v-model="activeNames" >
          <el-collapse-item  :title="item.title" :name="item.id" v-for="item in data.noticeData">
              <div>
                {{item.content}}
              </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      </div>

    <div v-else  style="margin-top: 10px;">
      <div style="display: flex ;grid-gap: 10px ;">
        <div class="card" style="height: 400px;width: 50%" id="pie"></div>
        <div class="card" style="height: 400px;width: 50%"  id="bar"></div>
      </div>
      <div class="card" style="height: 400px;width: 100%;margin-top: 10px"  id="line"></div>
    </div>

  </div>
</template>

<script setup>
import {reactive,onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import { ref } from 'vue'
import * as echarts from "echarts";


const data = reactive({
  user:JSON.parse(localStorage.getItem('quan_user') || '{}'),
  noticeData:[],

})

const loadNotice = () => {
  request.get('/notice/selectAll',).then(res => {
    if (res.code === '200'){
      data.noticeData = res.data
      if (data.noticeData.length > 3){
        data.noticeData = data.noticeData.slice(0,3)
      }
    }else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
const activeNames = ref(['1'])

const loadPie = () => {
  request.get('/echarts/pie').then(res => {
    if (res.code === '200'){
      let chartDom = document.getElementById('pie');
      let myChart = echarts.init(chartDom);
      pipOptions.series[0].data = res.data
      myChart.setOption(pipOptions);
    }
  })
}

const loadBar = () => {
  request.get('/echarts/bar').then(res => {
    if (res.code === '200'){
      let chartDom = document.getElementById('bar');
      let myChart = echarts.init(chartDom);
      barOptions.xAxis.data = res.data.xAxis
      barOptions.series[0].data = res.data.yAxis
      myChart.setOption(barOptions);
    }
  })
}

const loadLine = () => {
  request.get('/echarts/line').then(res => {
    if (res.code === '200'){
      let chartDom = document.getElementById('line');
      let myChart = echarts.init(chartDom);
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions);
    }
  })
}
onMounted(() => {
  loadPie()
  loadBar()
  loadLine()
})

//饼图
let pipOptions = {
  title: {
    text: '不同分类下会员发布健身攻略帖子的数量',
    subtext: '统计维度：会员昵称',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比',
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        { value: 1048, name: 'Search Engine' },
        { value: 735, name: 'Direct' },
        { value: 580, name: 'Email' },
        { value: 484, name: 'Union Ads' },
        { value: 300, name: 'Video Ads' }
      ],
    }
  ]
};

//柱状图
let barOptions = {
  title: {
    text: '不同用户发布帖子数量Top5',
    subtext: '统计维度：会员昵称',
    left: 'center'
  },
  grid :{
    bottom:'30%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    name: '会员昵称',
    axisLabel: {
      show: true, //是否显示刻度
      interval: 0, //坐标轴刻度标签的相关设置
      rotate: -60, // 旋转角度
      inside: false, // 刻度标签是否朝内
      margin: 6 //刻度标签与轴线之间的距离
    }
  },
  yAxis: {
    type: 'value',
    name: '攻略数量'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 显示数据
      type: 'bar',
      itemStyle: {
        //图形样式
        normal: {
          color: function (){
            return '#'+Math.floor(Math.random()* (256 * 256 * 256 - 1)).toString(16);
          }
        }
      },
    }
  ]
};

//折线图
let lineOptions = {
  title: {
    text: '近一周不同用会员发布帖子数量Top5',
    subtext: '统计维度：最近一周',
    left: 'center'
  },
  grid :{
    left:'3%',
    right:'4%',
    bottom:'3%',
    containLabel: true
  },
  legend: {
    data:[],
    template: ""
  },
  tooltip: {
    trigger: 'item'
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    name: '攻略数量',
    type: 'value'
  },
  series: [
    {
      name: '攻略数量',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true,
      markLine: {
        data: [
          [
            { type: 'max', name: '最大值' },
            { type: 'min', name: '最小值' }
          ]
        ]
      },
    }
  ]
};
</script>