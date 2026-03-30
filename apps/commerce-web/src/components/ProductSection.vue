<script setup lang="ts">
import type { Product } from '@/api/types'
import ProductCard from './ProductCard.vue'

defineProps<{
  title: string
  products: Product[]
  showRank?: boolean
}>()
</script>

<template>
  <section class="product-section">
    <div class="section-header">
      <h2 class="section-title">{{ title }}</h2>
      <router-link to="/products" class="view-all">전체보기 &rsaquo;</router-link>
    </div>
    <div class="product-grid">
      <ProductCard
        v-for="(product, index) in products"
        :key="product.id"
        :product="product"
        :rank="showRank ? index + 1 : undefined"
      />
    </div>
  </section>
</template>

<style scoped>
.product-section {
  padding: 24px 0 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 20px;
}

.section-title {
  font-size: 20px;
  font-weight: 800;
  letter-spacing: -0.3px;
}

.view-all {
  font-size: 13px;
  color: var(--gray-400);
  transition: color 0.15s;
}

.view-all:hover {
  color: var(--gray-600);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px 16px;
  padding: 0 20px;
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px 12px;
  }
}
</style>
