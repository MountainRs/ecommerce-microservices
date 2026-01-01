<template>
  <div class="space-y-12">
    <!-- 英雄区域 -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-800 text-white rounded-lg p-12">
      <h1 class="text-4xl font-bold mb-4">欢迎来到电商微服务平台</h1>
      <p class="text-xl mb-6">现代化的电商解决方案，采用微服务架构设计</p>
      <router-link
        to="/products"
        class="inline-block px-6 py-3 bg-white text-blue-600 font-semibold rounded-lg hover:bg-gray-100 transition-colors"
      >
        开始购物
      </router-link>
    </div>

    <!-- 特性介绍 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
      <div class="card">
        <div class="text-3xl mb-4">🚀</div>
        <h3 class="text-xl font-semibold mb-2">高性能</h3>
        <p class="text-gray-600">采用微服务架构，支持高并发访问</p>
      </div>

      <div class="card">
        <div class="text-3xl mb-4">🔒</div>
        <h3 class="text-xl font-semibold mb-2">安全可靠</h3>
        <p class="text-gray-600">JWT 无状态认证，数据加密存储</p>
      </div>

      <div class="card">
        <div class="text-3xl mb-4">📱</div>
        <h3 class="text-xl font-semibold mb-2">易于扩展</h3>
        <p class="text-gray-600">Docker 容器化，Kubernetes 编排</p>
      </div>
    </div>

    <!-- 热销商品 -->
    <div v-if="!loading" class="space-y-6">
      <h2 class="text-2xl font-bold">热销商品</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div
          v-for="product in products"
          :key="product.id"
          class="card hover:shadow-lg transition-shadow cursor-pointer"
          @click="goToProduct(product.id)"
        >
          <div class="h-40 bg-gray-200 rounded mb-4 flex items-center justify-center">
            <span class="text-gray-400">商品图片</span>
          </div>
          <h3 class="font-semibold text-lg mb-2">{{ product.name }}</h3>
          <p class="text-gray-600 text-sm mb-4 line-clamp-2">{{ product.description }}</p>
          <div class="flex justify-between items-center">
            <span class="text-2xl font-bold text-blue-600">¥{{ product.price }}</span>
            <span class="text-sm text-gray-500">库存: {{ product.stockQuantity }}</span>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-12">
      <p class="text-gray-600">加载中...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useProductStore } from '@/stores/product'

const router = useRouter()
const productStore = useProductStore()

const products = ref<any[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const response = await productStore.getAllProducts(0, 4)
    products.value = response.content
  } catch (error) {
    console.error('Failed to load products', error)
  } finally {
    loading.value = false
  }
})

const goToProduct = (productId: number) => {
  router.push(`/products/${productId}`)
}
</script>
