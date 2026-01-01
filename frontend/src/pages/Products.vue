<template>
  <div class="space-y-6">
    <h1 class="text-3xl font-bold">商品列表</h1>

    <!-- 搜索栏 -->
    <div class="flex gap-4">
      <input
        v-model="searchKeyword"
        type="text"
        placeholder="搜索商品..."
        class="form-input flex-1"
        @keyup.enter="handleSearch"
      />
      <button @click="handleSearch" class="btn-primary">
        搜索
      </button>
      <button @click="resetSearch" class="btn-secondary">
        重置
      </button>
    </div>

    <!-- 商品网格 -->
    <div v-if="!loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div
        v-for="product in products"
        :key="product.id"
        class="card hover:shadow-lg transition-shadow"
      >
        <div class="h-40 bg-gray-200 rounded mb-4 flex items-center justify-center">
          <span class="text-gray-400">商品图片</span>
        </div>
        <h3 class="font-semibold text-lg mb-2">{{ product.name }}</h3>
        <p class="text-gray-600 text-sm mb-4 line-clamp-2">{{ product.description }}</p>
        <div class="flex justify-between items-center mb-4">
          <span class="text-2xl font-bold text-blue-600">¥{{ product.price }}</span>
          <span class="text-sm text-gray-500">库存: {{ product.stockQuantity }}</span>
        </div>
        <button
          @click="goToProduct(product.id)"
          class="w-full btn-primary"
        >
          查看详情
        </button>
      </div>
    </div>

    <div v-else class="text-center py-12">
      <p class="text-gray-600">加载中...</p>
    </div>

    <!-- 分页 -->
    <div v-if="!loading" class="flex justify-center gap-4 mt-8">
      <button
        :disabled="currentPage === 0"
        @click="previousPage"
        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 disabled:opacity-50"
      >
        上一页
      </button>
      <span class="px-4 py-2">{{ currentPage + 1 }} / {{ totalPages }}</span>
      <button
        :disabled="currentPage >= totalPages - 1"
        @click="nextPage"
        class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300 disabled:opacity-50"
      >
        下一页
      </button>
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
const searchKeyword = ref('')
const currentPage = ref(0)
const pageSize = ref(12)
const totalPages = ref(0)

const loadProducts = async () => {
  loading.value = true
  try {
    if (searchKeyword.value) {
      const response = await productStore.searchProducts(searchKeyword.value, currentPage.value, pageSize.value)
      products.value = response.content
      totalPages.value = response.totalPages
    } else {
      const response = await productStore.getAllProducts(currentPage.value, pageSize.value)
      products.value = response.content
      totalPages.value = response.totalPages
    }
  } catch (error) {
    console.error('Failed to load products', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 0
  loadProducts()
}

const resetSearch = () => {
  searchKeyword.value = ''
  currentPage.value = 0
  loadProducts()
}

const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--
    loadProducts()
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
    loadProducts()
  }
}

const goToProduct = (productId: number) => {
  router.push(`/products/${productId}`)
}

onMounted(() => {
  loadProducts()
})
</script>
