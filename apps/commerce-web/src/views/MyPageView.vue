<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authStore, type AuthUser } from '@/stores/auth'
import { fetchMe } from '@/api/member'

const router = useRouter()
const user = ref<AuthUser | null>(null)
const loading = ref(true)

onMounted(async () => {
  if (!authStore.isLoggedIn.value) {
    router.push('/login')
    return
  }
  try {
    user.value = await fetchMe()
    authStore.setUser(user.value)
  } catch {
    authStore.logout()
    router.push('/login')
  } finally {
    loading.value = false
  }
})

function handleLogout() {
  authStore.logout()
  router.push('/')
}
</script>

<template>
  <div class="mypage">
    <div class="mypage-card">
      <h1 class="mypage-title">마이페이지</h1>

      <div v-if="loading" class="loading">불러오는 중...</div>

      <div v-else-if="user" class="profile">
        <div class="avatar">{{ user.name.charAt(0) }}</div>

        <div class="info-list">
          <div class="info-row">
            <span class="info-label">아이디</span>
            <span class="info-value">{{ user.loginId }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">이름</span>
            <span class="info-value">{{ user.name }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">생년월일</span>
            <span class="info-value">{{ user.birthDate }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">이메일</span>
            <span class="info-value">{{ user.email }}</span>
          </div>
        </div>

        <button class="logout-btn" @click="handleLogout">로그아웃</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mypage {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
}

.mypage-card {
  width: 100%;
  max-width: 480px;
}

.mypage-title {
  font-size: 24px;
  font-weight: 800;
  margin-bottom: 32px;
}

.loading {
  text-align: center;
  color: var(--gray-400);
  padding: 40px 0;
}

.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: var(--black);
  color: var(--white);
  font-size: 28px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 28px;
}

.info-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  border-top: 1px solid var(--gray-200);
}

.info-row {
  display: flex;
  padding: 16px 0;
  border-bottom: 1px solid var(--gray-100);
}

.info-label {
  width: 100px;
  flex-shrink: 0;
  font-size: 13px;
  font-weight: 600;
  color: var(--gray-500);
}

.info-value {
  font-size: 14px;
  color: var(--gray-900);
}

.logout-btn {
  margin-top: 32px;
  width: 100%;
  height: 44px;
  border: 1px solid var(--gray-300);
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-600);
  transition: all 0.15s;
}

.logout-btn:hover {
  border-color: var(--danger);
  color: var(--danger);
}
</style>
