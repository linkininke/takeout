// pages/cart/cart.js
const { post } = require('../../utils/request');
Page({
  data: {
    shopId: '',
    cartItems: [],
    totalPrice: 0,
    totalCount: 0
  },

  onLoad(options) {
    // 获取店铺ID，处理为空的情况
    this.setData({
      shopId: options.shopId || ''
    });
    // 加载购物车数据
    this.loadCart();
  },

  onShow() {
    // 页面显示时重新加载购物车数据
    this.loadCart();
  },

  // 加载购物车数据
  loadCart() {
    const cartItems = wx.getStorageSync('cart') || [];
    const shopCartItems = cartItems.filter(item => item.shopId === this.data.shopId);
    const totalCount = shopCartItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = shopCartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    
    this.setData({
      cartItems: shopCartItems,
      totalCount: totalCount,
      totalPrice: totalPrice
    });
  },

  // 增加商品数量
  increaseQuantity(e) {
    const dishId = e.currentTarget.dataset.id;
    let cartItems = wx.getStorageSync('cart') || [];
    
    // 找到该商品
    const itemIndex = cartItems.findIndex(item => item.id === dishId && item.shopId === this.data.shopId);
    
    if (itemIndex > -1) {
      // 数量+1
      cartItems[itemIndex].quantity += 1;
      // 保存到本地存储
      wx.setStorageSync('cart', cartItems);
      // 更新页面数据
      this.loadCart();
    }
  },

  // 减少商品数量
  decreaseQuantity(e) {
    const dishId = e.currentTarget.dataset.id;
    let cartItems = wx.getStorageSync('cart') || [];
    
    // 找到该商品
    const itemIndex = cartItems.findIndex(item => item.id === dishId && item.shopId === this.data.shopId);
    
    if (itemIndex > -1) {
      if (cartItems[itemIndex].quantity > 1) {
        // 数量>1，数量-1
        cartItems[itemIndex].quantity -= 1;
      } else {
        // 数量=1，从购物车移除
        cartItems.splice(itemIndex, 1);
      }
      // 保存到本地存储
      wx.setStorageSync('cart', cartItems);
      // 更新页面数据
      this.loadCart();
    }
  },

  // 删除商品
  deleteItem(e) {
    const dishId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '确定要删除该商品吗？',
      success: (res) => {
        if (res.confirm) {
          let cartItems = wx.getStorageSync('cart') || [];
          // 过滤掉要删除的商品
          cartItems = cartItems.filter(item => !(item.id === dishId && item.shopId === this.data.shopId));
          // 保存到本地存储
          wx.setStorageSync('cart', cartItems);
          // 更新页面数据
          this.loadCart();
        }
      }
    });
  },

  // 清空购物车
  clearCart() {
    wx.showModal({
      title: '提示',
      content: '确定要清空购物车吗？',
      success: (res) => {
        if (res.confirm) {
          let cartItems = wx.getStorageSync('cart') || [];
          // 过滤掉当前店铺的商品
          cartItems = cartItems.filter(item => item.shopId !== this.data.shopId);
          // 保存到本地存储
          wx.setStorageSync('cart', cartItems);
          // 更新页面数据
          this.loadCart();
        }
      }
    });
  },

  // 提交订单
  submitOrder() {
    if (this.data.totalCount === 0) {
      wx.showToast({
        title: '购物车为空',
        icon: 'error'
      });
      return;
    }

    // 检查是否登录
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '请先登录',
        success: (res) => {
          if (res.confirm) {
            wx.switchTab({
              url: '/pages/user/user'
            });
          }
        }
      });
      return;
    }

    // 构建订单数据
    const orderData = {
      shopId: this.data.shopId,
      totalAmount: this.data.totalPrice,
      totalQuantity: this.data.totalCount,
      orderItems: this.data.cartItems.map(item => ({
        dishId: item.id,
        quantity: item.quantity,
        price: item.price
      }))
    };

    // 调用API提交订单
    post('/orders/submit', orderData, { showLoading: true }).then((data) => {
      // 订单提交成功，跳转到订单确认页面
      wx.navigateTo({
        url: `/pages/order/confirm?id=${data.id}`
      });
      
      // 清空当前店铺的购物车
      let cartItems = wx.getStorageSync('cart') || [];
      cartItems = cartItems.filter(item => item.shopId !== this.data.shopId);
      wx.setStorageSync('cart', cartItems);
    }).catch((err) => {
      console.error('提交订单失败', err);
    });
  }
});
