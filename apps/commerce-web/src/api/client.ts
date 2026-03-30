import axios from 'axios'
import type { ApiResponse, Brand, Page, Product } from './types'

const apiClient = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

export async function fetchBrands(page = 0, size = 10): Promise<Page<Brand>> {
  const { data } = await apiClient.get<ApiResponse<Page<Brand>>>('/brands', {
    params: { page, size },
  })
  return data.data
}

export async function fetchProducts(params: {
  page?: number
  size?: number
  sort?: string
  brandId?: number
}): Promise<Page<Product>> {
  const { data } = await apiClient.get<ApiResponse<Page<Product>>>('/products', {
    params,
  })
  return data.data
}

export async function fetchProduct(productId: number): Promise<Product> {
  const { data } = await apiClient.get<ApiResponse<Product>>(`/products/${productId}`)
  return data.data
}

export default apiClient
