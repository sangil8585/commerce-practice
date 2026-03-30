<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const banners = [
  { id: 1, title: '2026 S/S 신상품', subtitle: '봄 시즌 트렌드를 한눈에', bg: '#1a1a2e', color: '#fff', accent: '#e94560' },
  { id: 2, title: '브랜드 위크', subtitle: '인기 브랜드 최대 70% OFF', bg: '#0f3460', color: '#fff', accent: '#16c79a' },
  { id: 3, title: '오늘만 이 가격', subtitle: '타임딜 상품을 확인하세요', bg: '#533483', color: '#fff', accent: '#e94560' },
]

const currentIndex = ref(0)
let timer: ReturnType<typeof setInterval>

function next() {
  currentIndex.value = (currentIndex.value + 1) % banners.length
}

function goTo(index: number) {
  currentIndex.value = index
}

onMounted(() => {
  timer = setInterval(next, 4000)
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<template>
  <section class="banner-wrapper">
    <div
      v-for="(banner, index) in banners"
      :key="banner.id"
      class="banner-slide"
      :class="{ active: index === currentIndex }"
      :style="{ background: banner.bg }"
    >
      <div class="banner-content">
        <span class="banner-badge" :style="{ background: banner.accent }">EVENT</span>
        <h2 class="banner-title" :style="{ color: banner.color }">{{ banner.title }}</h2>
        <p class="banner-subtitle" :style="{ color: banner.color, opacity: 0.8 }">{{ banner.subtitle }}</p>
        <button class="banner-cta" :style="{ borderColor: banner.color, color: banner.color }">
          자세히 보기 &rarr;
        </button>
      </div>
    </div>

    <div class="banner-dots">
      <button
        v-for="(_, index) in banners"
        :key="index"
        class="dot"
        :class="{ active: index === currentIndex }"
        @click="goTo(index)"
      />
    </div>

    <div class="banner-counter">
      {{ currentIndex + 1 }} / {{ banners.length }}
    </div>
  </section>
</template>

<style scoped>
.banner-wrapper {
  position: relative;
  width: 100%;
  height: 360px;
  overflow: hidden;
}

.banner-slide {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.6s ease;
}

.banner-slide.active {
  opacity: 1;
}

.banner-content {
  text-align: center;
}

.banner-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16px;
}

.banner-title {
  font-size: 36px;
  font-weight: 800;
  letter-spacing: -1px;
  margin-bottom: 8px;
}

.banner-subtitle {
  font-size: 16px;
  margin-bottom: 24px;
}

.banner-cta {
  padding: 10px 28px;
  border: 1.5px solid;
  border-radius: 24px;
  font-size: 14px;
  font-weight: 600;
  background: transparent;
  transition: all 0.2s;
}

.banner-cta:hover {
  background: rgba(255, 255, 255, 0.15);
}

.banner-dots {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  transition: all 0.3s;
}

.dot.active {
  width: 24px;
  border-radius: 4px;
  background: #fff;
}

.banner-counter {
  position: absolute;
  bottom: 20px;
  right: 24px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
  background: rgba(0, 0, 0, 0.3);
  padding: 4px 10px;
  border-radius: 12px;
}
</style>
