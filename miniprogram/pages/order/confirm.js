// pages/order/confirm.js
const { get, post } = require('../../utils/request');
Page({
  data: {
    orderId: '',
    orderInfo: null,
    addressList: [],
    selectedAddress: null
  },

  onLoad(options) {
    // 获取订单ID
    this.setData({
      orderId: options.id
    });
    // 加载订单信息
    this.loadOrderInfo();
    // 加载地址列表
    this.loadAddressList();
  },

  // 加载订单信息
  loadOrderInfo() {
    get(`/orders/${this.data.orderId}`, {}, { showLoading: true }).then((data) => {
      this.setData({
        orderInfo: data
      });
    }).catch((err) => {
      console.error('加载订单信息失败', err);
    });
  },

  // 加载地址列表
  loadAddressList() {
    get('/address-book/list', {}, { showLoading: true }).then((data) => {
      const addressList = data;
      // 默认选择第一个地址
      if (addressList.length > 0) {
        this.setData({
          addressList: addressList,
          selectedAddress: addressList[0]
        });
      }
    }).catch((err) => {
      console.error('加载地址列表失败', err);
    });
  },

  // 选择地址
  selectAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    const selectedAddress = this.data.addressList.find(address => address.id === addressId);
    this.setData({
      selectedAddress: selectedAddress
    });
  },

  // 发起微信支付
  async payOrder() {
    if (!this.data.selectedAddress) {
      wx.showToast({
        title: '请选择收货地址',
        icon: 'error'
      });
      return;
    }

    // 请求订阅消息
    await this.requestSubscribeMessage();

    const openid = wx.getStorageSync('customer')?.openid;

    if (!openid) {
      wx.showToast({
        title: '获取openid失败',
        icon: 'error'
      });
      return;
    }

    // 调用后端微信支付统一下单接口
    post(`/payment/wx-pay/${this.data.orderId}`, { openid: openid }, { showLoading: true }).then((data) => {
      const payParams = data;
      // 调用微信支付API
      this.callWechatPay(payParams);
    }).catch((err) => {
      console.error('获取支付参数失败', err);
      wx.showToast({
        title: '获取支付参数失败',
        icon: 'error'
      });
    });
  },

  // 请求订阅消息
  async requestSubscribeMessage() {
    const app = getApp();
    const tmplIds = [
      app.globalData.subscribeMessageTmplIds.orderStatus,
      app.globalData.subscribeMessageTmplIds.paymentSuccess,
      app.globalData.subscribeMessageTmplIds.deliveryStatus
    ];

    try {
      const res = await app.requestSubscribeMessage(tmplIds);
      console.log('订阅消息结果', res);
      // 可以根据订阅结果进行处理
    } catch (err) {
      console.error('订阅消息失败', err);
      // 订阅失败不影响支付流程
    }
  },

  // 调用微信支付API
  callWechatPay(payParams) {
    wx.requestPayment({
      timeStamp: payParams.timeStamp,
      nonceStr: payParams.nonceStr,
      package: payParams.package,
      signType: payParams.signType,
      paySign: payParams.paySign,
      success: (res) => {
        // 支付成功
        wx.showToast({
          title: '支付成功',
          icon: 'success'
        });
        // 跳转到订单列表页面
        setTimeout(() => {
          wx.switchTab({
            url: '/pages/order/order'
          });
        }, 1500);
      },
      fail: (err) => {
        // 支付失败
        console.error('支付失败', err);
        if (err.errMsg !== 'requestPayment:fail cancel') {
          wx.showToast({
            title: '支付失败',
            icon: 'error'
          });
        }
      }
    });
  }
});
