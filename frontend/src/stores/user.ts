import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import * as userApi from '@/api/user'

export interface User {
  id: number
  username: string
  email: string
  phone?: string
  realName?: string
  avatarUrl?: string
  status: string
  createdAt: string
  updatedAt: string
}

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const token = ref<string>('')
  const isLoggedIn = computed(() => !!token.value)

  /**
   * 从本地存储加载用户信息
   */
  const loadFromStorage = () => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')
    
    if (storedToken) {
      token.value = storedToken
    }
    
    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser)
      } catch (e) {
        console.error('Failed to parse stored user', e)
      }
    }
  }

  /**
   * 登录
   */
  const login = async (username: string, password: string) => {
    try {
      const response = await userApi.login({ username, password })
      token.value = response.token
      
      // 获取用户信息
      const userInfo = await userApi.getCurrentUser()
      user.value = userInfo
      
      // 保存到本地存储
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify(userInfo))
      
      return true
    } catch (error) {
      console.error('Login failed', error)
      return false
    }
  }

  /**
   * 注册
   */
  const register = async (data: userApi.RegisterRequest) => {
    try {
      const userInfo = await userApi.register(data)
      return userInfo
    } catch (error) {
      console.error('Register failed', error)
      throw error
    }
  }

  /**
   * 登出
   */
  const logout = () => {
    user.value = null
    token.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  /**
   * 获取当前用户信息
   */
  const getCurrentUser = async () => {
    try {
      const userInfo = await userApi.getCurrentUser()
      user.value = userInfo
      localStorage.setItem('user', JSON.stringify(userInfo))
      return userInfo
    } catch (error) {
      console.error('Failed to get current user', error)
      return null
    }
  }

  /**
   * 更新用户信息
   */
  const updateUserInfo = async (data: Partial<User>) => {
    if (!user.value) return null
    
    try {
      const updated = await userApi.updateUser(user.value.id, data)
      user.value = updated
      localStorage.setItem('user', JSON.stringify(updated))
      return updated
    } catch (error) {
      console.error('Failed to update user', error)
      throw error
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    loadFromStorage,
    login,
    register,
    logout,
    getCurrentUser,
    updateUserInfo,
  }
})
