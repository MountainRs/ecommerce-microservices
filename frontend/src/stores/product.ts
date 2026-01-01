import { defineStore } from 'pinia'
import { ref } from 'vue'
import * as productApi from '@/api/product'

export const useProductStore = defineStore('product', () => {
  const products = ref<productApi.ProductDto[]>([])
  const currentProduct = ref<productApi.ProductDto | null>(null)
  const total = ref(0)

  /**
   * 获取所有商品
   */
  const getAllProducts = async (page: number = 0, size: number = 10) => {
    try {
      const response = await productApi.getAllProducts(page, size)
      products.value = response.content
      total.value = response.totalElements
      return response
    } catch (error) {
      console.error('Failed to get products', error)
      throw error
    }
  }

  /**
   * 获取商品详情
   */
  const getProduct = async (productId: number) => {
    try {
      const product = await productApi.getProduct(productId)
      currentProduct.value = product
      return product
    } catch (error) {
      console.error('Failed to get product', error)
      throw error
    }
  }

  /**
   * 搜索商品
   */
  const searchProducts = async (keyword: string, page: number = 0, size: number = 10) => {
    try {
      const response = await productApi.searchProducts(keyword, page, size)
      products.value = response.content
      total.value = response.totalElements
      return response
    } catch (error) {
      console.error('Failed to search products', error)
      throw error
    }
  }

  /**
   * 根据分类获取商品
   */
  const getProductsByCategory = async (categoryId: number, page: number = 0, size: number = 10) => {
    try {
      const response = await productApi.getProductsByCategory(categoryId, page, size)
      products.value = response.content
      total.value = response.totalElements
      return response
    } catch (error) {
      console.error('Failed to get products by category', error)
      throw error
    }
  }

  /**
   * 创建商品
   */
  const createProduct = async (data: productApi.ProductDto) => {
    try {
      const product = await productApi.createProduct(data)
      products.value.push(product)
      return product
    } catch (error) {
      console.error('Failed to create product', error)
      throw error
    }
  }

  /**
   * 更新商品
   */
  const updateProduct = async (productId: number, data: Partial<productApi.ProductDto>) => {
    try {
      const updated = await productApi.updateProduct(productId, data)
      const index = products.value.findIndex(p => p.id === productId)
      if (index !== -1) {
        products.value[index] = updated
      }
      if (currentProduct.value?.id === productId) {
        currentProduct.value = updated
      }
      return updated
    } catch (error) {
      console.error('Failed to update product', error)
      throw error
    }
  }

  /**
   * 删除商品
   */
  const deleteProduct = async (productId: number) => {
    try {
      await productApi.deleteProduct(productId)
      products.value = products.value.filter(p => p.id !== productId)
      if (currentProduct.value?.id === productId) {
        currentProduct.value = null
      }
    } catch (error) {
      console.error('Failed to delete product', error)
      throw error
    }
  }

  return {
    products,
    currentProduct,
    total,
    getAllProducts,
    getProduct,
    searchProducts,
    getProductsByCategory,
    createProduct,
    updateProduct,
    deleteProduct,
  }
})
