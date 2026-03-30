import apiClient from './client'
import type { ApiResponse } from './types'
import { authStore, type AuthUser } from '@/stores/auth'

export interface SignupRequest {
  loginId: string
  password: string
  name: string
  birthDate: string
  email: string
}

export interface SignupResponse {
  loginId: string
  name: string
  birthDate: string
  email: string
}

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
}

export async function signup(req: SignupRequest): Promise<SignupResponse> {
  const { data } = await apiClient.post<ApiResponse<SignupResponse>>('/members/signup', req)
  return data.data
}

export async function fetchMe(): Promise<AuthUser> {
  const creds = authStore.getCredentials()
  if (!creds) throw new Error('로그인이 필요합니다.')

  const { data } = await apiClient.get<ApiResponse<AuthUser>>('/members/me', {
    headers: {
      'X-Loopers-LoginId': creds.loginId,
      'X-Loopers-LoginPw': creds.loginPw,
    },
  })
  return data.data
}

export async function login(loginId: string, loginPw: string): Promise<AuthUser> {
  const { data } = await apiClient.get<ApiResponse<AuthUser>>('/members/me', {
    headers: {
      'X-Loopers-LoginId': loginId,
      'X-Loopers-LoginPw': loginPw,
    },
  })
  authStore.setCredentials(loginId, loginPw)
  authStore.setUser(data.data)
  return data.data
}

export async function changePassword(req: ChangePasswordRequest): Promise<void> {
  const creds = authStore.getCredentials()
  if (!creds) throw new Error('로그인이 필요합니다.')

  await apiClient.patch('/members/me/password', req, {
    headers: {
      'X-Loopers-LoginId': creds.loginId,
      'X-Loopers-LoginPw': creds.loginPw,
    },
  })
}
