<script setup lang="ts">
import { ref } from 'vue'
import { authStore } from '@/stores/auth'

const searchQuery = ref('')
</script>

<template>
  <header class="header">
    <div class="header-inner">
      <router-link to="/" class="logo">LOOPERS</router-link>

      <div class="search-bar">
        <svg class="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8" />
          <path d="m21 21-4.35-4.35" />
        </svg>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="상품을 검색해보세요"
          class="search-input"
        />
      </div>

      <nav class="header-nav">
        <template v-if="!authStore.isLoggedIn.value">
          <router-link to="/login" class="nav-link">로그인</router-link>
          <router-link to="/signup" class="nav-link">회원가입</router-link>
        </template>
        <template v-else>
          <router-link to="/mypage" class="nav-link">마이페이지</router-link>
        </template>
        <router-link to="/cart" class="nav-link cart-link">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M6 2 3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z" />
            <line x1="3" y1="6" x2="21" y2="6" />
            <path d="M16 10a4 4 0 0 1-8 0" />
          </svg>
        </router-link>
      </nav>
    </div>

    <nav class="category-bar">
      <div class="category-inner">
        <button class="category-item active">추천</button>
        <button class="category-item">랭킹</button>
        <button class="category-item">신상품</button>
        <button class="category-item">브랜드</button>
        <button class="category-item">세일</button>
        <button class="category-item">상의</button>
        <button class="category-item">하의</button>
        <button class="category-item">아우터</button>
        <button class="category-item">신발</button>
        <button class="category-item">가방</button>
        <button class="category-item">액세서리</button>
      </div>
    </nav>
  </header>
</template>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
}

.header-inner {
  max-width: var(--max-width);
  margin: 0 auto;
  height: var(--header-height);
  display: flex;
  align-items: center;
  padding: 0 20px;
  gap: 24px;
}

.logo {
  font-size: 22px;
  font-weight: 900;
  letter-spacing: -0.5px;
  color: var(--black);
  flex-shrink: 0;
}

.search-bar {
  flex: 1;
  max-width: 500px;
  display: flex;
  align-items: center;
  background: var(--gray-100);
  border-radius: 8px;
  padding: 0 12px;
  height: 38px;
}

.search-icon {
  color: var(--gray-400);
  flex-shrink: 0;
}

.search-input {
  flex: 1;
  background: transparent;
  padding: 0 8px;
  font-size: 14px;
}

.search-input::placeholder {
  color: var(--gray-400);
}

.header-nav {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.nav-link {
  font-size: 13px;
  color: var(--gray-600);
  white-space: nowrap;
  transition: color 0.15s;
}

.nav-link:hover {
  color: var(--black);
}

.cart-link {
  display: flex;
  align-items: center;
}

.category-bar {
  border-top: 1px solid var(--gray-100);
  overflow-x: auto;
  scrollbar-width: none;
}

.category-bar::-webkit-scrollbar {
  display: none;
}

.category-inner {
  max-width: var(--max-width);
  margin: 0 auto;
  display: flex;
  padding: 0 20px;
  gap: 4px;
}

.category-item {
  padding: 10px 14px;
  font-size: 14px;
  font-weight: 500;
  color: var(--gray-500);
  white-space: nowrap;
  border-bottom: 2px solid transparent;
  transition: all 0.15s;
}

.category-item:hover,
.category-item.active {
  color: var(--black);
  border-bottom-color: var(--black);
}
</style>
