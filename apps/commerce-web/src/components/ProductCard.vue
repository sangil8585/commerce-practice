<script setup lang="ts">
import type { Product } from '@/api/types'

defineProps<{
  product: Product
  rank?: number
}>()

function formatPrice(price: number): string {
  return price.toLocaleString('ko-KR')
}
</script>

<template>
  <router-link :to="`/products/${product.id}`" class="product-card">
    <div class="product-thumb">
      <div class="thumb-placeholder">
        <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" opacity="0.3">
          <rect x="3" y="3" width="18" height="18" rx="2" ry="2" />
          <circle cx="8.5" cy="8.5" r="1.5" />
          <polyline points="21 15 16 10 5 21" />
        </svg>
      </div>
      <span v-if="rank" class="rank-badge">{{ rank }}</span>
      <button class="like-btn" @click.prevent>
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
        </svg>
      </button>
    </div>
    <div class="product-info">
      <p class="brand-name">{{ product.brandName }}</p>
      <p class="product-name">{{ product.name }}</p>
      <p class="product-price">{{ formatPrice(product.basePrice) }}원</p>
      <div class="product-meta">
        <span class="like-count">
          <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor" stroke="none">
            <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z" />
          </svg>
          {{ product.likeCount }}
        </span>
      </div>
    </div>
  </router-link>
</template>

<style scoped>
.product-card {
  display: block;
  cursor: pointer;
  transition: transform 0.15s;
}

.product-card:hover {
  transform: translateY(-2px);
}

.product-thumb {
  position: relative;
  aspect-ratio: 1;
  border-radius: 8px;
  overflow: hidden;
  background: var(--gray-100);
  margin-bottom: 10px;
}

.thumb-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--gray-400);
}

.rank-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  width: 28px;
  height: 28px;
  background: var(--black);
  color: var(--white);
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 800;
}

.like-btn {
  position: absolute;
  bottom: 8px;
  right: 8px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--gray-400);
  opacity: 0;
  transition: all 0.15s;
}

.product-card:hover .like-btn {
  opacity: 1;
}

.like-btn:hover {
  color: var(--danger);
}

.product-info {
  padding: 0 2px;
}

.brand-name {
  font-size: 13px;
  font-weight: 700;
  color: var(--gray-800);
  margin-bottom: 4px;
}

.product-name {
  font-size: 13px;
  color: var(--gray-500);
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.4;
}

.product-price {
  font-size: 15px;
  font-weight: 800;
  color: var(--black);
}

.product-meta {
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.like-count {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 12px;
  color: var(--gray-400);
}
</style>
