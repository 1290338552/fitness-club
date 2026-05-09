<template>
  <div class="mail-schedule">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>邮件定时任务管理</span>
        </div>
      </template>
      
      <div class="button-group">
        <el-button type="primary" @click="triggerSend">
          手动触发邮件发送
        </el-button>
        <el-button type="success" @click="testReminder">
          测试提醒邮件
        </el-button>
        <el-button type="warning" @click="createTestMail">
          创建30秒测试邮件
        </el-button>
        <el-button type="info" @click="testMailConfig">
          测试邮件配置
        </el-button>
      </div>
      
      <el-divider></el-divider>
      
      <div class="info-section">
        <h3>功能说明</h3>
        <ul>
          <li>当用户预约成功时，系统会自动创建提醒邮件任务</li>
          <li>每个预约会发送<strong>2次</strong>提醒邮件，间隔<strong>30秒</strong></li>
          <li>第1次提醒：立即发送，标题带"【第1次提醒】"</li>
          <li>第2次提醒：30秒后发送，标题带"【第2次提醒】"</li>
          <li>系统每30秒检查一次待发送的邮件</li>
          <li>邮件内容包含教练信息、课程费用、预约时间等</li>
        </ul>
      </div>
      
      <div class="cron-info">
        <h3>定时任务配置</h3>
        <p><strong>邮件发送检查：</strong>每30秒执行一次 (*/30 * * * * ?)</p>
        <p><strong>发送策略：</strong>每个预约发送2次，间隔30秒</p>
        <p><strong>清理旧记录：</strong>每天凌晨1点执行 (0 0 1 * * ?)</p>
      </div>
      
      <div class="test-section">
        <h3>测试功能</h3>
        <p>点击"创建30秒测试邮件"按钮可以快速测试定时邮件功能，系统将在30秒内发送2次测试邮件。</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'MailSchedule',
  methods: {
    async triggerSend() {
      try {
        const res = await request.post('/scheduledMail/triggerSend')
        if (res.code === '200') {
          this.$message.success('邮件发送任务已触发')
        } else {
          this.$message.error(res.msg || '触发失败')
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    async testReminder() {
      // 这里可以选择一个预约记录ID进行测试
      const recordId = prompt('请输入要测试的预约记录ID：')
      if (!recordId) return
      
      try {
        const res = await request.post(`/scheduledMail/createReminder/${recordId}`)
        if (res.code === '200') {
          this.$message.success('测试提醒邮件创建成功')
        } else {
          this.$message.error(res.msg || '创建失败')
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    async createTestMail() {
      const email = prompt('请输入测试邮箱地址：')
      if (!email) return
      
      // 简单的邮箱格式验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(email)) {
        this.$message.error('请输入有效的邮箱地址')
        return
      }
      
      try {
        const res = await request.post('/scheduledMail/createTestMail', null, {
          params: { email }
        })
        if (res.code === '200') {
          this.$message.success('测试邮件任务创建成功！将在30秒内发送2次邮件到 ' + email)
        } else {
          this.$message.error(res.msg || '创建失败')
        }
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },
    
    async testMailConfig() {
      const email = prompt('请输入测试邮箱地址（用于测试邮件配置）：')
      if (!email) return
      
      // 简单的邮箱格式验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(email)) {
        this.$message.error('请输入有效的邮箱地址')
        return
      }
      
      try {
        const res = await request.post('/mailTest/sendTest', null, {
          params: { toEmail: email }
        })
        if (res.code === '200') {
          this.$message.success('测试邮件发送成功！请检查邮箱：' + email)
        } else {
          this.$message.error(res.msg || '发送失败')
        }
      } catch (error) {
        this.$message.error('测试失败：' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.mail-schedule {
  padding: 20px;
}

.button-group {
  margin-bottom: 20px;
}

.button-group .el-button {
  margin-right: 10px;
}

.info-section, .cron-info, .test-section {
  margin-top: 20px;
}

.info-section ul {
  padding-left: 20px;
}

.info-section li {
  margin-bottom: 8px;
  line-height: 1.6;
}

.cron-info p {
  margin: 8px 0;
  font-family: 'Courier New', monospace;
}

.test-section p {
  color: #666;
  line-height: 1.6;
}
</style>