<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Brand, Product } from '@/api/types'
import { fetchBrands, fetchProducts } from '@/api/client'
import HeroBanner from '@/components/HeroBanner.vue'
import BrandList from '@/components/BrandList.vue'
import ProductSection from '@/components/ProductSection.vue'

const brands = ref<Brand[]>([])
const rankingProducts = ref<Product[]>([])
const newProducts = ref<Product[]>([])
const loading = ref(true)

// 더미 데이터 (API 연결 전 화면 확인용)
const dummyBrands: Brand[] = [
  { id: 1, name: 'Nike', description: '나이키', status: 'ACTIVE' },
  { id: 2, name: 'Adidas', description: '아디다스', status: 'ACTIVE' },
  { id: 3, name: 'New Balance', description: '뉴발란스', status: 'ACTIVE' },
  { id: 4, name: 'Musinsa Standard', description: '무신사 스탠다드', status: 'ACTIVE' },
  { id: 5, name: 'Covernat', description: '커버낫', status: 'ACTIVE' },
  { id: 6, name: 'Thisisneverthat', description: '디스이즈네버댓', status: 'ACTIVE' },
  { id: 7, name: 'Carhartt WIP', description: '칼하트', status: 'ACTIVE' },
  { id: 8, name: 'Stussy', description: '스투시', status: 'ACTIVE' },
]

const dummyProducts: Product[] = [
  { id: 1, brandId: 4, brandName: 'Musinsa Standard', name: '에센셜 릴렉스드 크루넥 반팔 티셔츠', description: '', basePrice: 12900, likeCount: 34521, status: 'ACTIVE' },
  { id: 2, brandId: 1, brandName: 'Nike', name: '에어포스 1 07 로우 화이트', description: '', basePrice: 139000, likeCount: 28930, status: 'ACTIVE' },
  { id: 3, brandId: 5, brandName: 'Covernat', name: 'C 로고 크루넥 스웻셔츠', description: '', basePrice: 59000, likeCount: 18420, status: 'ACTIVE' },
  { id: 4, brandId: 2, brandName: 'Adidas', name: '삼바 OG 화이트 블랙', description: '', basePrice: 129000, likeCount: 15200, status: 'ACTIVE' },
  { id: 5, brandId: 3, brandName: 'New Balance', name: '530 러닝 화이트 실버', description: '', basePrice: 129000, likeCount: 12340, status: 'ACTIVE' },
  { id: 6, brandId: 6, brandName: 'Thisisneverthat', name: 'T-로고 후디 블랙', description: '', basePrice: 89000, likeCount: 9870, status: 'ACTIVE' },
  { id: 7, brandId: 7, brandName: 'Carhartt WIP', name: '포켓 반팔 티셔츠 블랙', description: '', basePrice: 55000, likeCount: 8650, status: 'ACTIVE' },
  { id: 8, brandId: 8, brandName: 'Stussy', name: '베이직 스투시 후디', description: '', basePrice: 169000, likeCount: 7430, status: 'ACTIVE' },
]

const dummyNewProducts: Product[] = [
  { id: 9, brandId: 4, brandName: 'Musinsa Standard', name: '라이트 린넨 블렌드 오픈카라 반팔 셔츠', description: '', basePrice: 29900, likeCount: 320, status: 'ACTIVE' },
  { id: 10, brandId: 5, brandName: 'Covernat', name: '아치 로고 티셔츠 네이비', description: '', basePrice: 39000, likeCount: 210, status: 'ACTIVE' },
  { id: 11, brandId: 1, brandName: 'Nike', name: '드라이핏 트레이닝 반바지', description: '', basePrice: 45000, likeCount: 180, status: 'ACTIVE' },
  { id: 12, brandId: 3, brandName: 'New Balance', name: '574 레거시 그레이', description: '', basePrice: 119000, likeCount: 150, status: 'ACTIVE' },
]

onMounted(async () => {
  try {
    const [brandRes, rankingRes, newRes] = await Promise.allSettled([
      fetchBrands(0, 10),
      fetchProducts({ size: 8, sort: 'likeCount,desc' }),
      fetchProducts({ size: 4, sort: 'createdAt,desc' }),
    ])

    brands.value = brandRes.status === 'fulfilled' ? brandRes.value.content : dummyBrands
    rankingProducts.value = rankingRes.status === 'fulfilled' ? rankingRes.value.content : dummyProducts
    newProducts.value = newRes.status === 'fulfilled' ? newRes.value.content : dummyNewProducts
  } catch {
    // API 미연결 시 더미 데이터 사용
    brands.value = dummyBrands
    rankingProducts.value = dummyProducts
    newProducts.value = dummyNewProducts
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="home">
    <HeroBanner />

    <div class="home-content">
      <BrandList :brands="brands" />

      <div class="divider" />

      <ProductSection
        title="실시간 랭킹"
        :products="rankingProducts"
        :show-rank="true"
      />

      <div class="divider" />

      <ProductSection
        title="신상품"
        :products="newProducts"
      />
    </div>
  </div>
</template>

<style scoped>
.home-content {
  max-width: var(--max-width);
  margin: 0 auto;
}

.divider {
  height: 8px;
  background: var(--gray-100);
}
</style>
