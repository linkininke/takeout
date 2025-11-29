// pages/user/user.js
const { get, post } = require('../../utils/request');
Page({
  data: {
    userInfo: null,
    hasUserInfo: false,
    canIUseGetUserProfile: wx.canIUse('getUserProfile')
  },

  onLoad() {
    // 检查是否已登录
    const userInfo = wx.getStorageSync('userInfo')
    const token = wx.getStorageSync('token')
    if (userInfo && token) {
      this.setData({
        userInfo: userInfo,
        hasUserInfo: true
      })
      getApp().globalData.userInfo = userInfo
      getApp().globalData.token = token
    }
  },

  // 微信授权登录
  getUserProfile() {
    wx.getUserProfile({
      desc: '用于完善会员资料',
      success: (res) => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
        // 保存用户信息到本地存储
        wx.setStorageSync('userInfo', res.userInfo)
        getApp().globalData.userInfo = res.userInfo
        
        // 调用微信登录获取code
        this.wxLogin()
      }
    })
  },

  // 微信登录
  wxLogin() {
    wx.login({
      success: (res) => {
        if (res.code) {
          // 发送code到后台换取openid和token
          post('/auth/wx-login', {
            code: res.code
          }, { showLoading: true }).then((data) => {
            // 保存token到本地存储
            wx.setStorageSync('token', data.token)
            getApp().globalData.token = data.token
            // 保存顾客信息
            wx.setStorageSync('customer', data.customer)
            wx.showToast({
              title: '登录成功',
              icon: 'success'
            })
          }).catch((err) => {
            console.error('登录请求失败', err)
            wx.showToast({
              title: '网络错误',
              icon: 'error'
            })
          })
        } else {
          console.error('登录失败！' + res.errMsg)
          wx.showToast({
            title: '登录失败',
            icon: 'error'
          })
        }
      }
    })
  },

  // 跳转到订单页面
  navigateToOrder() {
    wx.switchTab({
      url: '/pages/order/order'
    })
  },
  
  // 跳转到地址管理页面
  navigateToAddress() {
    wx.navigateTo({
      url: '/pages/address/address'
    })
  },
  
  // 跳转到优惠券页面
  navigateToCoupon() {
    wx.navigateTo({
      url: '/pages/coupon/coupon'
    })
  },
  
  // 跳转到积分商城页面
  navigateToPointMall() {
    wx.navigateTo({
      url: '/pages/point-mall/point-mall'
    })
  },
  
  // 跳转到消息通知页面
  navigateToMessage() {
    wx.navigateTo({
      url: '/pages/message/message'
    })
  },
  
  // 跳转到客服中心页面
  navigateToCustomerService() {
    wx.navigateTo({
      url: '/pages/customer-service/customer-service'
    })
  },
  
  // 退出登录
  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除本地存储
          wx.removeStorageSync('userInfo')
          wx.removeStorageSync('token')
          wx.removeStorageSync('customer')
          // 清除全局数据
          getApp().globalData.userInfo = null
          getApp().globalData.token = null
          // 更新页面数据
          this.setData({
            userInfo: null,
            hasUserInfo: false
          })
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          })
        }
      }
    })
  }
})
