import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

// 创建axios实例（后端地址：http://localhost:9999）
const request = axios.create({
    baseURL: "http://localhost:9999",
    timeout: 30000,
});

// 请求拦截器：区分JSON/表单请求，不强制修改Content-Type
request.interceptors.request.use(config => {
    // 仅当请求体是「普通对象」时，才设置为JSON格式
    if (config.method === 'post' &&
        typeof config.data === 'object' &&
        !(config.data instanceof URLSearchParams) &&
        !(config.data instanceof FormData)) {
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
    }
    // 携带token（原有逻辑）
    let user = JSON.parse(localStorage.getItem('quan_user') || '{}')
    config.headers['token'] = user.token;
    return config
},  error => {
    return Promise.reject(error)
});

// 响应拦截器：统一处理返回结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 兼容字符串格式的响应
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 401：未登录/Token过期
        if (res.code === '401') {
            ElMessage.error(res.msg);
            router.push('/login');
            return Promise.reject(res.msg); // 补充返回，避免前端无响应
        } else {
            return res; // 正常响应：返回数据
        }
    },
    error => {
        // 统一错误提示
        if (error.response?.status === 404) {
            ElMessage.error('接口不存在');
        } else if (error.response?.status === 500) {
            ElMessage.error('后端系统错误');
        } else {
            ElMessage.error('请求失败：' + error.message);
        }
        return Promise.reject(error);
    }
);

export default request;