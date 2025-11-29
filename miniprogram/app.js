// app.js
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        console.log('登录成功', res.code)
      }
    })

    // 检查订阅消息权限
    this.checkSubscribeMessagePermission();
  },

  // 检查订阅消息权限
  checkSubscribeMessagePermission() {
    wx.getSetting({
      withSubscriptions: true,
      success: (res) => {
        console.log('订阅消息权限', res.subscriptionsSetting);
      }
    });
  },

  // 请求订阅消息
  requestSubscribeMessage(tmplIds) {
    return new Promise((resolve, reject) => {
      wx.requestSubscribeMessage({
        tmplIds: tmplIds,
        success: (res) => {
          resolve(res);
        },
        fail: (err) => {
          reject(err);
        }
      });
    });
  },

  globalData: {
    userInfo: null,
    token: null,
    shopInfo: null,
    // 订阅消息模板ID
    subscribeMessageTmplIds: {
      orderStatus: 'your-order-status-tmpl-id',
      paymentSuccess: 'your-payment-success-tmpl-id',
      deliveryStatus: 'your-delivery-status-tmpl-id'
    }
  }
})