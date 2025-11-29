// pages/shop/shop-detail.js
const { get } = require('../../utils/request');
Page({
  data: {
    shopId: '',
    shopInfo: null,
    dishes: [],
    categories: [],
    dishesByCategory: [],
    currentCategory: 0,
    cartItems: [],
    cartCount: 0,
    totalPrice: 0
  },

  onLoad(options) {
    // 获取店铺ID
    this.setData({
      shopId: options.id
    });
    // 加载店铺详情和菜品数据
    this.loadShopDetail();
    this.loadDishes();
    // 加载购物车数据
    this.loadCart();
  },

  // 加载店铺详情
  loadShopDetail() {
    get(`/shop/${this.data.shopId}`, {}, { showLoading: true }).then((data) => {
      this.setData({
        shopInfo: data
      });
    }).catch((err) => {
      console.error('加载店铺详情失败', err);
    });
  },

  // 加载菜品数据
  loadDishes() {
    get('/dish/list', { shopId: this.data.shopId }, { showLoading: true }).then((data) => {
      const dishes = data;
      // 提取分类
      const categoryNames = [...new Set(dishes.map(dish => dish.categoryName))];
      const categories = categoryNames.map((name, index) => ({
        id: index,
        name: name
      }));
      // 按分类组织菜品
      const dishesByCategory = categoryNames.map(name => ({
        name: name,
        items: dishes.filter(dish => dish.categoryName === name)
      }));
      this.setData({
        dishes: dishes,
        categories: categories,
        dishesByCategory: dishesByCategory
      });
    }).catch((err) => {
      console.error('加载菜品失败', err);
    });
  },

  // 加载购物车数据
  loadCart() {
    const cartItems = wx.getStorageSync('cart') || [];
    const shopCartItems = cartItems.filter(item => item.shopId === this.data.shopId);
    const cartCount = shopCartItems.reduce((sum, item) => sum + item.quantity, 0);
    const totalPrice = shopCartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    
    this.setData({
      cartItems: shopCartItems,
      cartCount: cartCount,
      totalPrice: totalPrice
    });
  },

  // 切换分类
  switchCategory(e) {
    this.setData({
      currentCategory: e.currentTarget.dataset.index
    });
  },

  // 添加到购物车
  addToCart(e) {
    const dish = e.currentTarget.dataset.dish;
    let cartItems = wx.getStorageSync('cart') || [];
    
    // 检查购物车中是否已有该菜品
    const existingItemIndex = cartItems.findIndex(item => item.id === dish.id && item.shopId === this.data.shopId);
    
    if (existingItemIndex > -1) {
      // 已有该菜品，数量+1
      cartItems[existingItemIndex].quantity += 1;
    } else {
      // 没有该菜品，添加到购物车
      cartItems.push({
        ...dish,
        shopId: this.data.shopId,
        quantity: 1
      });
    }
    
    // 保存到本地存储
    wx.setStorageSync('cart', cartItems);
    // 更新页面数据
    this.loadCart();
    
    wx.showToast({
      title: '已添加到购物车',
      icon: 'success',
      duration: 1000
    });
  },

  // 减少购物车数量
  reduceCart(e) {
    const dishId = e.currentTarget.dataset.id;
    let cartItems = wx.getStorageSync('cart') || [];
    
    // 找到该菜品
    const existingItemIndex = cartItems.findIndex(item => item.id === dishId && item.shopId === this.data.shopId);
    
    if (existingItemIndex > -1) {
      if (cartItems[existingItemIndex].quantity > 1) {
        // 数量>1，数量-1
        cartItems[existingItemIndex].quantity -= 1;
      } else {
        // 数量=1，从购物车移除
        cartItems.splice(existingItemIndex, 1);
      }
      
      // 保存到本地存储
      wx.setStorageSync('cart', cartItems);
      // 更新页面数据
      this.loadCart();
    }
  },

  // 跳转到购物车
  navigateToCart() {
    wx.navigateTo({
      url: '/pages/cart/cart?shopId=' + this.data.shopId
    });
  }
});
