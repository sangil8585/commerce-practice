export interface Brand {
  id: number
  name: string
  description: string
  status: string
}

export interface Product {
  id: number
  brandId: number
  brandName: string
  name: string
  description: string
  basePrice: number
  likeCount: number
  status: string
}

export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export interface ApiResponse<T> {
  status: string
  data: T
  message?: string
}
