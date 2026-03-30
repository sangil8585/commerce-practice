import { reactive, computed } from 'vue'

export interface AuthUser {
  loginId: string
  name: string
  birthDate: string
  email: string
}

interface AuthState {
  user: AuthUser | null
  loginId: string | null
  loginPw: string | null
}

const STORAGE_KEY = 'loopers_auth'

function loadFromStorage(): { loginId: string; loginPw: string } | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return null
    return JSON.parse(raw)
  } catch {
    return null
  }
}

const saved = loadFromStorage()

const state = reactive<AuthState>({
  user: null,
  loginId: saved?.loginId ?? null,
  loginPw: saved?.loginPw ?? null,
})

export const authStore = {
  state,

  isLoggedIn: computed(() => state.loginId !== null && state.loginPw !== null),

  getCredentials() {
    if (!state.loginId || !state.loginPw) return null
    return { loginId: state.loginId, loginPw: state.loginPw }
  },

  setCredentials(loginId: string, loginPw: string) {
    state.loginId = loginId
    state.loginPw = loginPw
    localStorage.setItem(STORAGE_KEY, JSON.stringify({ loginId, loginPw }))
  },

  setUser(user: AuthUser) {
    state.user = user
  },

  logout() {
    state.user = null
    state.loginId = null
    state.loginPw = null
    localStorage.removeItem(STORAGE_KEY)
  },
}
