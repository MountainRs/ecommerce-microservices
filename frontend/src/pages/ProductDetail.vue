<template>
  <div class="space-y-6">
    <router-link to="/products" class="text-blue-600 hover:text-blue-800">
      ← 返回商品列表
    </router-link>

    <div v-if="!loading && product" class="grid grid-cols-1 md:grid-cols-2 gap-8">
      <!-- 商品图片 -->
      <div class="card">
        <div class="h-96 bg-gray-200 rounded flex items-center justify-center">
          <span class="text-gray-400 text-xl">商品图片</span>
        </div>
      </div>

      <!-- 商品信息 -->
      <div class="space-y-6">
        <div>
          <h1 class="text-3xl font-bold mb-2">{{ product.name }}</h1>
          <div class="flex items-center gap-4">
            <span class="text-4xl font-bold text-blue-600">¥{{ product.price }}</span>
            <span class="text-lg text-gray-600">库存: {{ product.stockQuantity }}</span>
          </div>
        </div>

        <div class="card">
          <h3 class="font-semibold mb-2">商品描述</h3>
          <p class="text-gray-600">{{ product.description }}</p>
        </div>

        <div class="card">
          <h3 class="font-semibold mb-4">商品信息</h3>
          <div class="space-y-2 text-sm">
            <p><span class="font-medium">分类 ID:</span> {{ product.categoryId }}</p>
            <p><span class="font-medium">状态:</span> {{ product.status }}</p>
            <p><span class="font-medium">创建时间:</span> {{ formatDate(product.createdAt) }}</p>
            <p><span class="font-medium">更新时间:</span> {{ formatDate(product.updatedAt) }}</p>
          </div>
        </div>

        <div class="flex gap-4">
          <button
            :disabled="product.stockQuantity === 0"
            class="flex-1 btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
          >
            加入购物车
          </button>
          <button class="flex-1 btn-secondary">
            收藏
          </button>
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
import { useRoute } from 'vue-router'
import { useProductStore } from '@/stores/product'

const route = useRoute()
const productStore = useProductStore()

const product = ref<any>(null)
const loading = ref(true)

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

onMounted(async () => {
  try {
    const productId = parseInt(route.params.id as string)
    product.value = await productStore.getProduct(productId)
  } catch (error) {
    console.error('Failed to load product', error)
  } finally {
    loading.value = false
  }
})
</script>
