<template>
  <div>
    <div class="card">
      <div style="margin-bottom: 10px">
        <el-input clearable @clear="load" style="width: 260px;margin-right: 5px" v-model="data.optUser" placeholder="请输入操作人查询" :prefix-icon="Search"></el-input>
        <el-select style="width: 260px;margin-right: 5px" v-model="data.type" placeholder="请选择操作类型">
          <el-option v-for="item in ['登录','注册']" :key="item" :value="item" :label="item"></el-option>
        </el-select>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button clearable type="warning" @click="reset">重 置</el-button>
      </div>
      <div>
        <el-input clearable @clear="resetMailInput" style="width: 260px;margin-right: 5px" v-model="mailSearchUser" placeholder="请输入操作人发送预警" :prefix-icon="Search"></el-input>
        <el-button type="success" icon="Message" @click="openMailDialogBySearch">发 送</el-button>
        <el-button clearable type="warning" @click="resetMailInput">重 置</el-button>
      </div>
    </div>

    <div class="card">
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%"
                @selection-change="handleSelectionChange"
                :header-cell-style="{color:'#333',backgroundColor:'#f5f7fa'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="operation" label="操作模块" width="180" />
        <el-table-column prop="type" label="操作类型" >
          <template #default="scope">
            <el-tag
                :type="scope.row.type === '登录' ? 'success' : 'warning'"
                size="small"
            >
              {{ scope.row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="操作人IP" />
        <el-table-column prop="user" label="操作人" />
        <el-table-column prop="time" label="最后登录时间" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
            <!-- 每行新增发送预警按钮 -->
            <el-button type="success" icon="Message" circle @click="openMailDialog(scope.row)" style="margin-left: 8px"></el-button>
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

    <!-- 邮件发送弹窗 -->
    <el-dialog
        v-model="mailDialogVisible"
        title="发送登录预警邮件"
        width="600px"
        destroy-on-close
        @close="resetMailForm"
    >
      <el-form
          ref="mailFormRef"
          :model="mailForm"
          :rules="mailRules"
          label-width="80px"
      >
        <el-form-item label="发送人邮箱" prop="from">
          <el-input
              v-model="mailForm.from"
              placeholder="请输入发送人邮箱（需与后端配置一致）"
              clearable
          />
        </el-form-item>
        <el-form-item label="收件人邮箱" prop="to">
          <el-input
              v-model="mailForm.to"
              placeholder="单个邮箱：zhangsan@163.com | 多个邮箱：zhangsan@163.com,lisi@qq.com"
              clearable
              readonly
          />
          <div style="font-size: 12px; color: #999; margin-top: 4px;">
            提示：多个邮箱请用【英文逗号】分隔，勿用中文逗号/空格
          </div>
        </el-form-item>
        <el-form-item label="邮件主题" prop="subject">
          <el-input
              v-model="mailForm.subject"
              placeholder="请输入邮件主题（默认：登录预警通知）"
              clearable
          />
        </el-form-item>
        <el-form-item label="邮件内容" prop="content">
          <el-input
              v-model="mailForm.content"
              type="textarea"
              :rows="8"
              placeholder="请输入预警邮件内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="mailDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="sendMail" :loading="mailLoading">发 送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue"
import { Search, Message, Delete } from "@element-plus/icons-vue"
import axios from "axios";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

// 原有数据
const data = reactive({
  user: JSON.parse(localStorage.getItem('quan_user') || '{}'),
  optUser: null,
  type: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {},
  rows: [],
  ids: []
})

// 新增：邮件相关响应式数据
const mailDialogVisible = ref(false)
const mailLoading = ref(false)
const mailFormRef = ref(null)
const mailSearchUser = ref('') // 搜索框绑定的操作人
const mailForm = reactive({
  from: '',
  to: '',
  subject: '登录预警通知',
  content: ''
})

// 邮件表单校验规则
const mailRules = reactive({
  from: [
    { required: true, message: '请输入发送人邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入合法的邮箱地址', trigger: 'blur' }
  ],
  to: [
    { required: true, message: '请输入收件人邮箱', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value) {
          const emails = value.split(',').map(item => item.trim())
          const reg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
          const invalid = emails.some(email => !reg.test(email))
          if (invalid) {
            callback(new Error('收件人邮箱格式错误（多个邮箱用逗号分隔）'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  subject: [
    { required: true, message: '请输入邮件主题', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入邮件内容', trigger: 'blur' }
  ]
})

// 原有方法
const formRef = ref()
const load = () => {
  request.get("/logs/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      user: data.optUser,
      type: data.type
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const reset = () => {
  data.optUser = null
  load()
}

const handleSelectionChange = (rows) => {
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const del = (id) => {
  ElMessageBox.confirm('此操作将永久删除该数据吗, 是否继续?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/logs/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => { })
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning("请选择要删除的数据")
    return
  }
  ElMessageBox.confirm('此操作将永久删除该数据吗, 是否继续?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/logs/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success("批量删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => { })
}

// 新增：邮件相关方法
// 重置邮件搜索框
const resetMailInput = () => {
  mailSearchUser.value = ''
}

// 根据行数据打开邮件弹窗（每行操作按钮）
const openMailDialog = async (row) => {
  try {
    // 根据操作人查询邮箱
    const res = await request.get(`/user/getEmailByUsername?username=${row.user}`)
    if (res.code === '200' && res.data) {
      // 填充收件人邮箱和内容
      mailForm.to = res.data
      mailForm.content = `【登录预警】\n操作人：${row.user}\n操作类型：${row.type}\n操作IP：${row.ip}\n操作时间：${row.time}`
      mailDialogVisible.value = true
    } else {
      ElMessage.warning(`未查询到操作人${row.user}的邮箱信息`)
    }
  } catch (err) {
    ElMessage.error("查询操作人邮箱失败，请稍后重试")
  }
}

// 根据搜索框操作人打开邮件弹窗
const openMailDialogBySearch = async () => {
  if (!mailSearchUser.value) {
    ElMessage.warning("请输入要发送预警的操作人")
    return
  }
  try {
    // 根据搜索框的操作人查询邮箱
    const res = await request.get(`/user/getEmailByUsername?username=${mailSearchUser.value}`)
    if (res.code === '200' && res.data) {
      // 填充收件人邮箱
      mailForm.to = res.data
      mailForm.content = `【登录预警】\n操作人：${mailSearchUser.value}\n预警时间：${new Date().toLocaleString()}`
      mailDialogVisible.value = true
    } else {
      ElMessage.warning(`未查询到操作人${mailSearchUser.value}的邮箱信息`)
    }
  } catch (err) {
    ElMessage.error("查询操作人邮箱失败，请稍后重试")
  }
}

// 重置邮件表单
const resetMailForm = () => {
  mailFormRef.value?.resetFields()
  mailForm.from = ''
  mailForm.to = ''
  mailForm.subject = '登录预警通知'
  mailForm.content = ''
}

// 发送邮件
// 前端sendMail方法中，修改参数拼接方式：
// 发送邮件方法（在<script setup>中）
const sendMail = async () => {
  try {
    // 1. 前端表单校验（必填项）
    await mailFormRef.value.validate();
    // 兜底判断：避免校验绕过
    if (!mailForm.from || !mailForm.to) {
      ElMessage.error("发送人/收件人邮箱不能为空！");
      return;
    }
    mailLoading.value = true;

    // 2. 打印前端参数（调试用：确认有值）
    console.log("前端要发送的参数：", mailForm);

    // 3. 构建表单参数（URLSearchParams格式）
    const formData = new URLSearchParams();
    formData.append('from', mailForm.from.trim()); // 去空格
    formData.append('to', mailForm.to.trim());
    formData.append('subject', mailForm.subject.trim());
    formData.append('content', mailForm.content.trim());

    // 4. 发送请求（指定表单格式Content-Type）
    const res = await request.post('/mail/sendSimpleMail', formData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
      }
    });

    // 5. 处理响应
    if (res.code === '200') {
      ElMessage.success('邮件发送成功！');
      mailDialogVisible.value = false; // 关闭弹窗
      resetMailForm(); // 重置表单
    } else {
      ElMessage.error('邮件发送失败：' + (res.msg || res.data));
    }
  } catch (err) {
    // 捕获请求异常（如网络错误、接口404）
    console.error("请求错误详情：", err); // 打印详细错误
    ElMessage.error('邮件发送失败：' + (err.response?.data?.msg || err.message));
  } finally {
    mailLoading.value = false; // 关闭加载状态
  }
};
</script>

<style scoped>
.card {
  background: #fff;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
}
</style>