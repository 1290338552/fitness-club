<template>
  <div class="bg">
    <div style="width: 350px;background-color: #d9e7f4;opacity: 0.95;border-radius: 5px;box-shadow: 0 0 10px rgba(0,0,0,0.1);padding: 40px 20px">
      <!-- 登录方式选项卡（参考参考代码优化样式） -->
      <div class="login-tabs" style="display: flex; margin-bottom: 30px; border-bottom: 1px solid #eee;">
        <div
            class="tab-item"
            :class="{ active: loginType === 'account' }"
            @click="switchLoginType('account')"
            style="flex: 1; text-align: center; padding: 10px 0; cursor: pointer; font-size: 16px; color: #666; position: relative;"
        >
          账号密码登录
        </div>
        <div
            class="tab-item"
            :class="{ active: loginType === 'phone' }"
            @click="switchLoginType('phone')"
            style="flex: 1; text-align: center; padding: 10px 0; cursor: pointer; font-size: 16px; color: #666; position: relative;"
        >
          手机验证码登录
        </div>
      </div>

      <!-- 表单容器（统一ref管理，优化验证逻辑） -->
      <el-form
          :ref="formRefName"
          :model="data.form"
          :rules="loginType === 'account' ? data.accountRules : data.phoneRules"
          label-width="0px"
      >
        <div style="margin-bottom: 25px;text-align: center;font-weight: 600;font-size: 24px; color: #333;">
          {{ loginType === 'account' ? '欢迎登录' : '手机验证码登录' }}
        </div>

        <!-- 账号密码登录表单 -->
        <template v-if="loginType === 'account'">
          <el-form-item prop="username">
            <el-input
                size="large"
                v-model="data.form.username"
                autocomplete="off"
                prefix-icon="User"
                placeholder="请输入账号"
                class="input"
                clearable
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                size="large"
                show-password
                v-model="data.form.password"
                autocomplete="off"
                prefix-icon="Lock"
                placeholder="请输入密码"
                class="input"
                clearable
            />
          </el-form-item>

          <el-form-item prop="code">
            <div style="display: flex; gap: 10px; align-items: center;">
              <el-input
                  size="large"
                  style="height: 42px; flex: 1;"
                  placeholder="请输入图形验证码"
                  v-model="sidentifyMode"
                  prefix-icon="Lock"
                  clearable
              />
              <!-- 使用验证码组件 -->
              <div class="code" @click="refreshCode" style="width: 120px; height: 42px;">
                <SIdentify :identifyCode="identifyCode" />
              </div>
            </div>
          </el-form-item>

          <el-form-item prop="role">
            <el-select
                size="large"
                style="width: 100%;"
                v-model="data.form.role"
                placeholder="请选择角色"
                class="select"
            >
              <el-option label="管理员" value="ADMIN"></el-option>
              <el-option label="会员" value="USER"></el-option>
              <el-option label="教练" value="COACH"></el-option>
            </el-select>
          </el-form-item>
        </template>

        <!-- 手机验证码登录表单（参考参考代码优化布局和交互） -->
        <template v-else>
          <el-form-item prop="phone">
            <el-input
                size="large"
                v-model="data.form.phone"
                autocomplete="off"
                prefix-icon="Phone"
                placeholder="请输入手机号码"
                maxlength="11"
                class="input"
                clearable
                @input="formatPhone"
            />
          </el-form-item>

          <el-form-item prop="smsCode">
            <div style="display: flex; gap: 10px; align-items: center;">
              <el-input
                  size="large"
                  v-model="data.form.smsCode"
                  autocomplete="off"
                  prefix-icon="Message"
                  placeholder="请输入4位验证码"
                  maxlength="4"
                  class="input"
                  clearable
                  @input="onlyNumber"
              />
              <el-button
                  size="large"
                  type="primary"
                  style="width: 120px; height: 42px; padding: 0;"
                  :disabled="countdown > 0 || !isPhoneValid"
                  :loading="sending"
                  @click="getSmsCode"
              >
                {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
              </el-button>
            </div>
          </el-form-item>

          <el-form-item prop="role">
            <el-select
                size="large"
                style="width: 100%;"
                v-model="data.form.role"
                placeholder="请选择角色"
                class="select"
            >
              <el-option label="管理员" value="ADMIN"></el-option>
              <el-option label="会员信息" value="USER"></el-option>
            </el-select>
          </el-form-item>
        </template>

        <div style="margin: 30px 0 15px;">
          <el-button
              style="width: 100%; height: 44px; font-size: 16px;"
              size="large"
              type="primary"
              @click="login"
              :loading="loading"
          >
            登录
          </el-button>
        </div>
        <div style="text-align: right; margin-top: 10px; font-size: 14px;">
          还没有账号？请<a style="color: #537bee; cursor: pointer; text-decoration: none;" @click="$router.push('/register')">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, computed, onUnmounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import router from "@/router/index.js";
import SIdentify from '@/components/Sidentify.vue';
import { useRouter } from 'vue-router';

// 表单ref动态名称（根据登录类型切换）
const formRefName = computed(() => loginType.value === 'account' ? 'accountFormRef' : 'phoneFormRef');
const accountFormRef = ref();
const phoneFormRef = ref();
const formRef = computed(() => loginType.value === 'account' ? accountFormRef.value : phoneFormRef.value);

const loading = ref(false); // 全局加载状态
const sending = ref(false); // 验证码发送加载状态

// 路由实例
const $router = useRouter();

// 图形验证码相关
let sidentifyMode = ref(''); // 输入的图形验证码
let identifyCode = ref(''); // 生成的图形验证码
const identifyCodes = ref('1234567890abcdefjhijklinopqrsduvwxyz'); // 验证码字符库

// 登录类型：account-账号密码，phone-手机验证码
let loginType = ref('account');

// 短信验证码相关
let countdown = ref(0); // 倒计时秒数
let timer = ref(null); // 倒计时计时器

// 组件挂载时生成图形验证码
onMounted(() => {
  refreshCode();
});

// 组件卸载时清除计时器
onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value);
  }
});

// 切换登录类型（参考参考代码优化重置逻辑）
const switchLoginType = (type) => {
  loginType.value = type;
  // 重置对应表单
  if (type === 'account' && accountFormRef.value) {
    accountFormRef.value.resetFields();
    sidentifyMode.value = '';
    refreshCode(); // 刷新图形验证码
  } else if (type === 'phone' && phoneFormRef.value) {
    phoneFormRef.value.resetFields();
    countdown.value = 0; // 重置倒计时
  }
};

// 手机号格式验证（计算属性）
const isPhoneValid = computed(() => {
  const phone = data.form.phone.trim();
  return /^1[3-9]\d{9}$/.test(phone);
});

// 生成随机数（图形验证码用）
const randomNum = (min, max) => {
  max = max + 1;
  return Math.floor(Math.random() * (max - min) + min);
};

// 随机生成图形验证码字符串
const makeCode = (o, l) => {
  let code = '';
  for (let i = 0; i < l; i++) {
    code += o[randomNum(0, o.length - 1)];
  }
  return code;
};

// 刷新图形验证码
const refreshCode = () => {
  identifyCode.value = makeCode(identifyCodes.value, 4);
  sidentifyMode.value = ''; // 清空输入框
};

// 格式化手机号输入（只允许数字）
const formatPhone = (val) => {
  data.form.phone = val.replace(/\D/g, ''); // 过滤非数字字符
};

// 验证码只允许数字输入
const onlyNumber = (val) => {
  data.form.smsCode = val.replace(/\D/g, ''); // 过滤非数字字符
};

// 获取短信验证码（优化加载状态和错误处理）
const getSmsCode = async () => {
  const phone = data.form.phone.trim();
  if (!isPhoneValid.value) {
    ElMessage.error('请输入正确的手机号');
    return;
  }

  try {
    sending.value = true;
    // 保持原有接口：/api/sms/send-code
    const response = await request({
      url: '/api/sms/send-code',
      method: 'POST',
      data: { phoneNumber: phone }
    });

    if (response.code === '200') {
      ElMessage.success(response.data || '验证码发送成功，请注意查收');
      startCountdown(); // 启动倒计时
    } else {
      ElMessage.error(response.data || '验证码发送失败');
    }
  } catch (error) {
    console.error('发送验证码失败:', error);
    ElMessage.error('网络异常，请稍后重试');
  } finally {
    sending.value = false;
  }
};

// 启动倒计时（优化计时器逻辑）
const startCountdown = () => {
  countdown.value = 60; // 60秒倒计时
  if (timer.value) {
    clearInterval(timer.value); // 清除原有计时器
  }
  timer.value = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer.value);
      timer.value = null;
    }
  }, 1000);
};

// 表单数据和验证规则（参考参考代码优化规则）
const data = reactive({
  form: {
    role: 'ADMIN',
    username: '',
    password: '',
    phone: '', // 手机号
    smsCode: '', // 短信验证码
  },
  // 账号密码登录规则
  accountRules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 1, max: 20, message: '账号长度为1-20位', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 1, max: 20, message: '密码长度为1-20位', trigger: 'blur' }
    ],

    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ]
  },
  // 手机验证码登录规则（修正为4位验证码，匹配日志中的2359）
  phoneRules: {
    phone: [
      { required: true, message: '请输入手机号', trigger: 'blur' },
      { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
    ],
    smsCode: [
      { required: true, message: '请输入验证码', trigger: 'blur' },
      { min: 4, max: 4, message: '验证码长度为4位', trigger: 'blur' },
      { pattern: /^\d{4}$/, message: '验证码必须为4位数字', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ]
  }
});

// 登录处理（保持原有接口，优化逻辑流程）
const login = async () => {
  // 【修复 Render 报错】
  let currentForm = null
  if (loginType.value === 'account') {
    currentForm = accountFormRef.value
  } else {
    currentForm = phoneFormRef.value
  }

  if (!currentForm) {
    setTimeout(() => login(), 100)
    return
  }

  currentForm.validate(async (valid) => {
    if (valid) {
      // 校验图形验证码
      if (loginType.value === 'account') {
        if (sidentifyMode.value.toLowerCase() !== identifyCode.value.toLowerCase()) {
          ElMessage.error('图形验证码错误')
          refreshCode()
          return
        }
      }
      loading.value = true;
      try {
        if (loginType.value === 'account') {
          // 账号密码登录：保持原有接口 /login
          const res = await request.post('/login', data.form);
          if (res.code === '200') {
            // 1. 保存完整的用户信息
            localStorage.setItem('quan_user', JSON.stringify(res.data));
            // 2. 单独保存token（如果存在）
            if (res.data.token) {
              localStorage.setItem('token', res.data.token);
            } else {
              console.warn('登录返回的用户信息中没有token字段');
            }
            ElMessage.success("登录成功");
            handleRoleJump(data.form.role); // 统一角色跳转逻辑
          } else {
            ElMessage.error(res.data || '登录失败');
          }
        } else {
          const { phone, smsCode, role } = data.form;

          // 1. 验证码验证：保持原有接口 /api/sms/verify-code
          const verifyResponse = await request({
            url: '/api/sms/verify-code',
            method: 'POST',
            data: {
              phoneNumber: phone,
              code: smsCode
            }
          });

          if (verifyResponse.code === '200') {
            ElMessage.success('验证码验证通过，正在登录...');

            // 2. 手机登录：保持原有接口 /api/login/phone
            const loginResponse = await request({
              url: '/phone',
              method: 'POST',
              data: {
                phoneNumber: phone,
                role: role,
              }
            });

            if (loginResponse.code === '200') {
              ElMessage.success('登录成功！');
              localStorage.setItem('quan_user', JSON.stringify(loginResponse.data || {}));
              handleRoleJump(role); // 统一角色跳转逻辑
            } else {
              ElMessage.error(loginResponse.data || '登录失败');
            }
          } else {
            ElMessage.error(verifyResponse.data || '验证码验证失败');
            data.form.smsCode = ''; // 清空验证码输入框
          }
        }
      } catch (error) {
        console.error('登录失败:', error);
        ElMessage.error('网络异常，请稍后重试');
      } finally {
        loading.value = false;
      }
    }
  });
};

// 统一角色跳转逻辑（提取公共方法）
const handleRoleJump = (role) => {
  if (role === "USER") {
    ElMessageBox.confirm(
        '请选择要进入的系统',
        '登录成功',
        {
          confirmButtonText: '后台管理',
          cancelButtonText: '前台页面',
          type: 'success',
          distinguishCancelAndClose: true
        }
    ).then(() => {
      router.push('/');
    }).catch(action => {
      if (action === 'cancel') {
        location.href = '/front/home';
      }
    });
  } else if (role === "COACH") {
    // 教练跳转到教练后台
    router.push('/coachManager/home');
  } else {
    // 管理员跳转到后台管理
    router.push('/');
  }
};
</script>

<style scoped>
.bg {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.png");
  background-size: cover;
  margin: 0;
  padding: 20px;
  box-sizing: border-box;
}

/* 选项卡激活样式（参考参考代码优化） */
.login-tabs .tab-item.active {
  color: #537bee;
  font-weight: 500;
}
.login-tabs .tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #537bee;
}

/* 输入框和选择器样式优化 */
.input {
  height: 42px;
  border-radius: 4px;
  border-color: #eaeaea;
  transition: border-color 0.3s;
}
.input:focus-within {
  border-color: #537bee;
  box-shadow: 0 0 0 2px rgba(83, 123, 238, 0.1);
}

.select {
  height: 42px;
  border-radius: 4px;
  border-color: #eaeaea;
}
.select:focus-within {
  border-color: #537bee;
  box-shadow: 0 0 0 2px rgba(83, 123, 238, 0.1);
}

/* 图形验证码样式 */
.code {
  cursor: pointer;
  overflow: hidden;
  border-radius: 4px;
  border: 1px solid #eaeaea;
}

/* 加载状态优化 */
.el-loading-mask {
  z-index: 9999 !important;
}

/* 按钮样式优化 */
.el-button--primary.is-disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.el-button--primary {
  background-color: #537bee;
  border-color: #537bee;
}
.el-button--primary:hover {
  background-color: #4366d0;
  border-color: #4366d0;
}
</style>
