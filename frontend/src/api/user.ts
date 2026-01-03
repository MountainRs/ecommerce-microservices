import http from './http'

export interface LoginRequest {
  usersname: string
  password: string
}

export interface LoginResponse {
  usersId: number
  usersname: string
  token: string
  tokenType: string
  expiresIn: number
}

export interface RegisterRequest {
  usersname: string
  email: string
  password: string
  confirmPassword: string
  phone?: string
  realName?: string
}

export interface usersInfo {
  id: number
  usersname: string
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
export const register = (data: RegisterRequest): Promise<usersInfo> => {
  return http.post('/users/register', data)
}

/**
 * 获取当前用户信息
 */
export const getCurrentusers = (): Promise<usersInfo> => {
  return http.get('/users/me')
}

/**
 * 根据 ID 获取用户信息
 */
export const getusersById = (usersId: number): Promise<usersInfo> => {
  return http.get(`/users/${usersId}`)
}

/**
 * 更新用户信息
 */
export const updateusers = (usersId: number, data: Partial<usersInfo>): Promise<usersInfo> => {
  return http.put(`/users/${usersId}`, data)
}
