import http from './http'

export interface ProductDto {
  id?: number
  name: string
  description?: string
  categoryId: number
  price: number
  stockQuantity?: number
  status?: string
  imageUrl?: string
  createdAt?: string
  updatedAt?: string
}

export interface PaginatedResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  currentPage: number
  pageSize: number
}

/**
 * 创建商品
 */
export const createProduct = (data: ProductDto): Promise<ProductDto> => {
  return http.post('/products', data)
}

/**
 * 获取商品详情
 */
export const getProduct = (productId: number): Promise<ProductDto> => {
  return http.get(`/products/${productId}`)
}

/**
 * 分页获取所有商品
 */
export const getAllProducts = (page: number = 0, size: number = 10): Promise<PaginatedResponse<ProductDto>> => {
  return http.get('/products', {
    params: { page, size },
  })
}

/**
 * 根据分类获取商品
 */
export const getProductsByCategory = (categoryId: number, page: number = 0, size: number = 10): Promise<PaginatedResponse<ProductDto>> => {
  return http.get(`/products/category/${categoryId}`, {
    params: { page, size },
  })
}

/**
 * 搜索商品
 */
export const searchProducts = (keyword: string, page: number = 0, size: number = 10): Promise<PaginatedResponse<ProductDto>> => {
  return http.get('/products/search', {
    params: { keyword, page, size },
  })
}

/**
 * 更新商品
 */
export const updateProduct = (productId: number, data: Partial<ProductDto>): Promise<ProductDto> => {
  return http.put(`/products/${productId}`, data)
}

/**
 * 删除商品
 */
export const deleteProduct = (productId: number): Promise<void> => {
  return http.delete(`/products/${productId}`)
}

/**
 * 更新商品库存
 */
export const updateStock = (productId: number, quantity: number): Promise<void> => {
  return http.put(`/products/${productId}/stock`, null, {
    params: { quantity },
  })
}
