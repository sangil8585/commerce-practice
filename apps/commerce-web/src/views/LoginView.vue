<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/member'

const router = useRouter()

const loginId = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

async function handleLogin() {
  error.value = ''

  if (!loginId.value.trim()) {
    error.value = '아이디를 입력해주세요.'
    return
  }
  if (!password.value) {
    error.value = '비밀번호를 입력해주세요.'
    return
  }

  loading.value = true
  try {
    await login(loginId.value.trim(), password.value)
    router.push('/')
  } catch (e: unknown) {
    if (e && typeof e === 'object' && 'response' in e) {
      const resp = e as { response?: { status?: number } }
      if (resp.response?.status === 401) {
        error.value = '아이디 또는 비밀번호가 일치하지 않습니다.'
      } else if (resp.response?.status === 404) {
        error.value = '존재하지 않는 아이디입니다.'
      } else {
        error.value = '로그인에 실패했습니다. 다시 시도해주세요.'
      }
    } else {
      error.value = '서버에 연결할 수 없습니다.'
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <h1 class="login-logo">LOOPERS</h1>
      <p class="login-subtitle">로그인</p>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="loginId">아이디</label>
          <input
            id="loginId"
            v-model="loginId"
            type="text"
            placeholder="아이디를 입력하세요"
            autocomplete="username"
          />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            autocomplete="current-password"
          />
        </div>

        <p v-if="error" class="error-msg">{{ error }}</p>

        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '로그인 중...' : '로그인' }}
        </button>
      </form>

      <div class="login-footer">
        <span class="footer-text">아직 회원이 아니신가요?</span>
        <router-link to="/signup" class="signup-link">회원가입</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - var(--header-height) - 200px);
  padding: 40px 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.login-logo {
  font-size: 32px;
  font-weight: 900;
  text-align: center;
  letter-spacing: -0.5px;
  margin-bottom: 4px;
}

.login-subtitle {
  text-align: center;
  font-size: 15px;
  color: var(--gray-500);
  margin-bottom: 32px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 13px;
  font-weight: 600;
  color: var(--gray-700);
}

.form-group input {
  height: 44px;
  padding: 0 14px;
  border: 1px solid var(--gray-200);
  border-radius: 8px;
  font-size: 14px;
  background: var(--white);
  transition: border-color 0.15s;
}

.form-group input:focus {
  border-color: var(--black);
}

.form-group input::placeholder {
  color: var(--gray-400);
}

.error-msg {
  font-size: 13px;
  color: var(--danger);
  text-align: center;
}

.login-btn {
  height: 48px;
  background: var(--black);
  color: var(--white);
  font-size: 15px;
  font-weight: 700;
  border-radius: 8px;
  margin-top: 4px;
  transition: opacity 0.15s;
}

.login-btn:hover:not(:disabled) {
  opacity: 0.85;
}

.login-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 13px;
}

.footer-text {
  color: var(--gray-400);
}

.signup-link {
  color: var(--black);
  font-weight: 700;
  margin-left: 6px;
  text-decoration: underline;
}
</style>
