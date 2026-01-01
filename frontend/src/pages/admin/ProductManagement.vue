<template>
  <div class="space-y-6">
    <div class="flex justify-between items-center">
      <h1 class="text-3xl font-bold">商品管理</h1>
      <button @click="showCreateForm = true" class="btn-primary">
        + 添加商品
      </button>
    </div>

    <!-- 商品表格 -->
    <div v-if="!loading" class="card overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50 border-b">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-semibold">商品名称</th>
            <th class="px-6 py-3 text-left text-sm font-semibold">价格</th>
            <th class="px-6 py-3 text-left text-sm font-semibold">库存</th>
            <th class="px-6 py-3 text-left text-sm font-semibold">状态</th>
            <th class="px-6 py-3 text-left text-sm font-semibold">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
            <td class="px-6 py-4">{{ product.name }}</td>
            <td class="px-6 py-4">¥{{ product.price }}</td>
            <td class="px-6 py-4">{{ product.stockQuantity }}</td>
            <td class="px-6 py-4">
              <span
                :class="{
                  'px-2 py-1 rounded text-sm font-medium': true,
                  'bg-green-100 text-green-800': product.status === 'active',
                  'bg-gray-100 text-gray-800': product.status !== 'active',
                }"
              >
                {{ product.status }}
              </span>
            </td>
            <td class="px-6 py-4 space-x-2">
              <button @click="editProduct(product)" class="text-blue-600 hover:text-blue-800">
                编辑
              </button>
              <button @click="deleteProduct(product.id)" class="text-red-600 hover:text-red-800">
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else class="text-center py-12">
      <p class="text-gray-600">加载中...</p>
    </div>

    <!-- 创建/编辑表单 -->
    <div v-if="showCreateForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div class="bg-white rounded-lg p-8 max-w-md w-full space-y-4">
        <h2 class="text-2xl font-bold">{{ editingProduct ? '编辑商品' : '添加商品' }}</h2>

        <div>
          <label class="form-label">商品名称</label>
          <input v-model="form.name" type="text" class="form-input" />
        </div>

        <div>
          <label class="form-label">价格</label>
          <input v-model.number="form.price" type="number" step="0.01" class="form-input" />
        </div>

        <div>
          <label class="form-label">库存</label>
          <input v-model.number="form.stockQuantity" type="number" class="form-input" />
        </div>

        <div>
          <label class="form-label">描述</label>
          <textarea v-model="form.description" class="form-input" rows="4"></textarea>
        </div>

        <div class="flex gap-4">
          <button @click="handleSave" class="flex-1 btn-primary">
            保存
          </button>
          <button @click="closeForm" class="flex-1 btn-secondary">
            取消
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useProductStore } from '@/stores/product'

const productStore = useProductStore()

const products = ref<any[]>([])
const loading = ref(true)
const showCreateForm = ref(false)
const editingProduct = ref<any>(null)

const form = ref({
  name: '',
  price: 0,
  stockQuantity: 0,
  description: '',
  categoryId: 1,
})

const loadProducts = async () => {
  loading.value = true
  try {
    const response = await productStore.getAllProducts(0, 100)
    products.value = response.content
  } catch (error) {
    console.error('Failed to load products', error)
  } finally {
    loading.value = false
  }
}

const editProduct = (product: any) => {
  editingProduct.value = product
  form.value = {
    name: product.name,
    price: product.price,
    stockQuantity: product.stockQuantity,
    description: product.description,
    categoryId: product.categoryId,
  }
  showCreateForm.value = true
}

const handleSave = async () => {
  try {
    if (editingProduct.value) {
      await productStore.updateProduct(editingProduct.value.id, form.value)
    } else {
      await productStore.createProduct(form.value)
    }
    closeForm()
    loadProducts()
  } catch (error) {
    console.error('Failed to save product', error)
  }
}

const deleteProduct = async (productId: number) => {
  if (confirm('确定要删除这个商品吗？')) {
    try {
      await productStore.deleteProduct(productId)
      loadProducts()
    } catch (error) {
      console.error('Failed to delete product', error)
    }
  }
}

const closeForm = () => {
  showCreateForm.value = false
  editingProduct.value = null
  form.value = {
    name: '',
    price: 0,
    stockQuantity: 0,
    description: '',
    categoryId: 1,
  }
}

onMounted(() => {
  loadProducts()
})
</script>
