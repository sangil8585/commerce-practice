<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { signup } from '@/api/member'

const router = useRouter()

const form = ref({
  loginId: '',
  password: '',
  passwordConfirm: '',
  name: '',
  birthDate: '',
  email: '',
})
const error = ref('')
const loading = ref(false)

async function handleSignup() {
  error.value = ''

  if (!form.value.loginId.trim()) { error.value = '아이디를 입력해주세요.'; return }
  if (!form.value.password) { error.value = '비밀번호를 입력해주세요.'; return }
  if (form.value.password !== form.value.passwordConfirm) { error.value = '비밀번호가 일치하지 않습니다.'; return }
  if (!form.value.name.trim()) { error.value = '이름을 입력해주세요.'; return }
  if (!form.value.birthDate) { error.value = '생년월일을 입력해주세요.'; return }
  if (!form.value.email.trim()) { error.value = '이메일을 입력해주세요.'; return }

  loading.value = true
  try {
    await signup({
      loginId: form.value.loginId.trim(),
      password: form.value.password,
      name: form.value.name.trim(),
      birthDate: form.value.birthDate,
      email: form.value.email.trim(),
    })
    alert('회원가입이 완료되었습니다. 로그인해주세요.')
    router.push('/login')
  } catch (e: unknown) {
    if (e && typeof e === 'object' && 'response' in e) {
      const resp = e as { response?: { status?: number; data?: { message?: string } } }
      if (resp.response?.status === 409) {
        error.value = '이미 사용 중인 아이디입니다.'
      } else if (resp.response?.status === 400) {
        error.value = resp.response?.data?.message || '입력 정보를 확인해주세요.'
      } else {
        error.value = '회원가입에 실패했습니다.'
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
  <div class="signup-page">
    <div class="signup-card">
      <h1 class="signup-logo">LOOPERS</h1>
      <p class="signup-subtitle">회원가입</p>

      <form class="signup-form" @submit.prevent="handleSignup">
        <div class="form-group">
          <label for="loginId">아이디</label>
          <input id="loginId" v-model="form.loginId" type="text" placeholder="영문, 숫자 조합" autocomplete="username" />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input id="password" v-model="form.password" type="password" placeholder="8~16자, 영문/숫자/특수문자" autocomplete="new-password" />
        </div>

        <div class="form-group">
          <label for="passwordConfirm">비밀번호 확인</label>
          <input id="passwordConfirm" v-model="form.passwordConfirm" type="password" placeholder="비밀번호 재입력" autocomplete="new-password" />
        </div>

        <div class="form-group">
          <label for="name">이름</label>
          <input id="name" v-model="form.name" type="text" placeholder="이름을 입력하세요" />
        </div>

        <div class="form-group">
          <label for="birthDate">생년월일</label>
          <input id="birthDate" v-model="form.birthDate" type="date" />
        </div>

        <div class="form-group">
          <label for="email">이메일</label>
          <input id="email" v-model="form.email" type="email" placeholder="example@email.com" />
        </div>

        <p v-if="error" class="error-msg">{{ error }}</p>

        <button type="submit" class="signup-btn" :disabled="loading">
          {{ loading ? '가입 중...' : '가입하기' }}
        </button>
      </form>

      <div class="signup-footer">
        <span class="footer-text">이미 회원이신가요?</span>
        <router-link to="/login" class="login-link">로그인</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.signup-page {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
}

.signup-card {
  width: 100%;
  max-width: 400px;
}

.signup-logo {
  font-size: 32px;
  font-weight: 900;
  text-align: center;
  letter-spacing: -0.5px;
  margin-bottom: 4px;
}

.signup-subtitle {
  text-align: center;
  font-size: 15px;
  color: var(--gray-500);
  margin-bottom: 32px;
}

.signup-form {
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

.signup-btn {
  height: 48px;
  background: var(--black);
  color: var(--white);
  font-size: 15px;
  font-weight: 700;
  border-radius: 8px;
  margin-top: 4px;
  transition: opacity 0.15s;
}

.signup-btn:hover:not(:disabled) {
  opacity: 0.85;
}

.signup-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.signup-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 13px;
}

.footer-text {
  color: var(--gray-400);
}

.login-link {
  color: var(--black);
  font-weight: 700;
  margin-left: 6px;
  text-decoration: underline;
}
</style>
