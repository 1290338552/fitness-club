<template>
  <div>
    <!-- 余额卡片 -->
    <div class="card" style="display:flex;align-items:center;gap:30px">
      <div style="text-align:center">
        <div style="font-size:14px;color:#999;margin-bottom:6px">当前余额</div>
        <div style="font-size:36px;font-weight:bold;color:#7F55B1">¥{{ balance.toFixed(2) }}</div>
      </div>
      <el-divider direction="vertical" style="height:60px" />
      <div style="display:flex;align-items:center;gap:10px">
        <el-input-number v-model="rechargeAmount" :min="1" :max="9999" :precision="2" style="width:160px" />
        <el-button type="primary" @click="doRecharge" :loading="loading">立即充值</el-button>
      </div>
    </div>

    <!-- 流水记录 -->
    <div class="card">
      <div style="font-size:15px;font-weight:bold;margin-bottom:12px">收支记录</div>
      <el-table :data="records" style="width:100%" :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column prop="createTime" label="时间" width="180" />
        <el-table-column prop="type" label="类型" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.type === 'RECHARGE' ? 'success' : 'danger'">
              {{ scope.row.type === 'RECHARGE' ? '充值' : '消费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="scope">
            <span :style="{ color: scope.row.type === 'RECHARGE' ? '#67c23a' : '#f56c6c', fontWeight: 'bold' }">
              {{ scope.row.type === 'RECHARGE' ? '+' : '-' }}¥{{ scope.row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request.js'

const balance = ref(0)
const records = ref([])
const rechargeAmount = ref(100)
const loading = ref(false)

const loadBalance = () => {
  request.get('/recharge/balance').then(res => {
    if (res.code === '200') balance.value = Number(res.data) || 0
  })
}

const loadRecords = () => {
  request.get('/recharge/records').then(res => {
    if (res.code === '200') records.value = res.data || []
  })
}

const doRecharge = () => {
  loading.value = true
  const userId = JSON.parse(localStorage.getItem('quan_user') || '{}').id
  request.get('/pay/create', { params: { amount: rechargeAmount.value, userId } }).then(res => {
    if (res.code === '200') {
      // 把支付宝返回的 HTML 表单写入页面并自动提交，跳转到支付宝收银台
      const div = document.createElement('div')
      div.innerHTML = res.data
      document.body.appendChild(div)
      document.forms[document.forms.length - 1].submit()
    } else {
      ElMessage.error(res.msg)
    }
  }).finally(() => { loading.value = false })
}

onMounted(() => {
  loadBalance()
  loadRecords()
})
</script>
