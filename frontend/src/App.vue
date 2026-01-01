<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <!-- 导航栏 -->
    <nav class="bg-white shadow-md">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <!-- Logo -->
          <div class="flex-shrink-0">
            <router-link to="/" class="text-2xl font-bold text-blue-600">
              电商平台
            </router-link>
          </div>

          <!-- 导航链接 -->
          <div class="hidden md:flex space-x-8">
            <router-link to="/" class="text-gray-600 hover:text-gray-900">
              首页
            </router-link>
            <router-link to="/products" class="text-gray-600 hover:text-gray-900">
              商品
            </router-link>
            <router-link v-if="userStore.isLoggedIn" to="/admin/products" class="text-gray-600 hover:text-gray-900">
              管理
            </router-link>
          </div>

          <!-- 用户菜单 -->
          <div class="flex items-center space-x-4">
            <template v-if="userStore.isLoggedIn">
              <span class="text-gray-700">{{ userStore.user?.username }}</span>
              <router-link to="/profile" class="text-blue-600 hover:text-blue-800">
                个人资料
              </router-link>
              <button @click="logout" class="text-red-600 hover:text-red-800">
                登出
              </button>
            </template>
            <template v-else>
              <router-link to="/login" class="text-blue-600 hover:text-blue-800">
                登录
              </router-link>
              <router-link to="/register" class="text-blue-600 hover:text-blue-800">
                注册
              </router-link>
            </template>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主内容 -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <router-view />
    </main>

    <!-- 页脚 -->
    <footer class="bg-gray-800 text-white mt-12">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <p class="text-center">© 2024 电商微服务平台. 保留所有权利。</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

// 初始化用户信息
userStore.loadFromStorage()

// 登出
const logout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
#app {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
</style>
