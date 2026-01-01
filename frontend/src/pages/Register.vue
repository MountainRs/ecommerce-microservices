<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          用户注册
        </h2>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleRegister">
        <div class="space-y-4">
          <div>
            <label for="username" class="form-label">用户名</label>
            <input
              id="username"
              v-model="form.username"
              type="text"
              required
              class="form-input"
              placeholder="请输入用户名"
            />
          </div>

          <div>
            <label for="email" class="form-label">邮箱</label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              required
              class="form-input"
              placeholder="请输入邮箱"
            />
          </div>

          <div>
            <label for="password" class="form-label">密码</label>
            <input
              id="password"
              v-model="form.password"
              type="password"
              required
              class="form-input"
              placeholder="请输入密码"
            />
          </div>

          <div>
            <label for="confirmPassword" class="form-label">确认密码</label>
            <input
              id="confirmPassword"
              v-model="form.confirmPassword"
              type="password"
              required
              class="form-input"
              placeholder="请确认密码"
            />
          </div>

          <div>
            <label for="realName" class="form-label">真实姓名（可选）</label>
            <input
              id="realName"
              v-model="form.realName"
              type="text"
              class="form-input"
              placeholder="请输入真实姓名"
            />
          </div>

          <div>
            <label for="phone" class="form-label">手机号（可选）</label>
            <input
              id="phone"
              v-model="form.phone"
              type="tel"
              class="form-input"
              placeholder="请输入手机号"
            />
          </div>
        </div>

        <div v-if="error" class="rounded-md bg-red-50 p-4">
          <p class="text-sm font-medium text-red-800">{{ error }}</p>
        </div>

        <div>
          <button
            type="submit"
            :disabled="loading"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
          >
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </div>

        <div class="text-center">
          <p class="text-sm text-gray-600">
            已有账户?
            <router-link to="/login" class="font-medium text-blue-600 hover:text-blue-500">
              立即登录
            </router-link>
          </p>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
})

const loading = ref(false)
const error = ref('')

const handleRegister = async () => {
  // 验证表单
  if (!form.value.username || !form.value.email || !form.value.password || !form.value.confirmPassword) {
    error.value = '请填写所有必填项'
    return
  }

  if (form.value.password !== form.value.confirmPassword) {
    error.value = '两次输入的密码不一致'
    return
  }

  if (form.value.password.length < 6) {
    error.value = '密码长度至少为 6 个字符'
    return
  }

  loading.value = true
  error.value = ''

  try {
    await userStore.register({
      username: form.value.username,
      email: form.value.email,
      password: form.value.password,
      confirmPassword: form.value.confirmPassword,
      realName: form.value.realName,
      phone: form.value.phone,
    })

    // 注册成功后自动登录
    const success = await userStore.login(form.value.username, form.value.password)
    if (success) {
      router.push('/')
    } else {
      error.value = '注册成功，请登录'
      router.push('/login')
    }
  } catch (err) {
    error.value = '注册失败，请稍后重试'
    console.error(err)
  } finally {
    loading.value = false
  }
}
</script>
