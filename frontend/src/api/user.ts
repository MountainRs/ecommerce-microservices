import http from './http'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  userId: number
  username: string
  token: string
  tokenType: string
  expiresIn: number
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  confirmPassword: string
  phone?: string
  realName?: string
}

export interface UserInfo {
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

/**
 * 用户登录
 */
export const login = (data: LoginRequest): Promise<LoginResponse> => {
  return http.post('/users/login', data)
}

/**
 * 用户注册
 */
export const register = (data: RegisterRequest): Promise<UserInfo> => {
  return http.post('/users/register', data)
}

/**
 * 获取当前用户信息
 */
export const getCurrentUser = (): Promise<UserInfo> => {
  return http.get('/users/me')
}

/**
 * 根据 ID 获取用户信息
 */
export const getUserById = (userId: number): Promise<UserInfo> => {
  return http.get(`/users/${userId}`)
}

/**
 * 更新用户信息
 */
export const updateUser = (userId: number, data: Partial<UserInfo>): Promise<UserInfo> => {
  return http.put(`/users/${userId}`, data)
}
