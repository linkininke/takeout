import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: (() => {
      const userInfoStr = localStorage.getItem('userInfo');
      try {
        return userInfoStr ? JSON.parse(userInfoStr) : null;
      } catch (e) {
        return null;
      }
    })(),
    token: localStorage.getItem('token') || ''
  }),
  getters: {
    isLoggedIn: (state) => !!state.token
  },
  actions: {
    setUserInfo(userInfo) {
      this.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
    },
    logout() {
      this.userInfo = null
      this.token = ''
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
    }
  }
})