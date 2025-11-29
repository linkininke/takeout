import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import { useUserStore } from '../store/modules/user'

const axiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
axiosInstance.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
axiosInstance.interceptors.response.use(
    response => {
      const res = response.data
      if (res.code === 1 || res.code === 200) {
        return res
      } else {
        ElMessage.error(res.message || '请求失败')
        return Promise.reject(new Error(res.message || '请求失败'))
      }
    },
    error => {
    if (error.response) {
      const { status } = error.response
      if (status === 401) {
        ElMessage.error('未授权，请重新登录')
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
      } else if (status === 403) {
        ElMessage.error('没有权限访问该资源')
      } else if (status === 500) {
        ElMessage.error('服务器内部错误')
      } else {
        ElMessage.error(error.response.data.message || '请求失败')
      }
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
    return Promise.reject(error)
  }
)

export default axiosInstance