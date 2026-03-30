<script setup lang="ts">
import type { Brand } from '@/api/types'

defineProps<{
  brands: Brand[]
}>()

const brandColors = ['#1a1a2e', '#16213e', '#0f3460', '#533483', '#e94560', '#2b2d42', '#8d99ae']

function getColor(index: number): string {
  return brandColors[index % brandColors.length]
}

function getInitial(name: string): string {
  return name.charAt(0).toUpperCase()
}
</script>

<template>
  <section class="brand-section">
    <div class="section-header">
      <h2 class="section-title">인기 브랜드</h2>
      <router-link to="/brands" class="view-all">전체보기 &rsaquo;</router-link>
    </div>
    <div class="brand-scroll">
      <router-link
        v-for="(brand, index) in brands"
        :key="brand.id"
        :to="`/brands/${brand.id}`"
        class="brand-item"
      >
        <div class="brand-logo" :style="{ background: getColor(index) }">
          {{ getInitial(brand.name) }}
        </div>
        <span class="brand-name">{{ brand.name }}</span>
      </router-link>
    </div>
  </section>
</template>

<style scoped>
.brand-section {
  padding: 32px 0;
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

.brand-scroll {
  display: flex;
  gap: 20px;
  padding: 0 20px;
  overflow-x: auto;
  scrollbar-width: none;
}

.brand-scroll::-webkit-scrollbar {
  display: none;
}

.brand-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.brand-logo {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 22px;
  font-weight: 800;
  transition: transform 0.15s;
}

.brand-item:hover .brand-logo {
  transform: scale(1.08);
}

.brand-name {
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-700);
  white-space: nowrap;
}
</style>
