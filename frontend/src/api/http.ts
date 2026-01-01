import axios, { AxiosInstance, AxiosRequestConfig } from 'axios'

// 创建 axios 实例
const http: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
http.interceptors.request.use(
  (config) => {
    // 从本地存储获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
http.interceptors.response.use(
  (response) => {
    const { code, data, message } = response.data
    
    // 如果响应码不是 200，则抛出错误
    if (code !== 200) {
      console.error(`API Error: ${message}`)
      return Promise.reject(new Error(message || '请求失败'))
    }
    
    return data
  },
  (error) => {
    if (error.response?.status === 401) {
      // Token 过期或无效，清除本地存储并重定向到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default http
