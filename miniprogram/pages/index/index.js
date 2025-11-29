// index.js
const { post } = require('../../utils/request');
const app = getApp();

Page({
  data: {
    isLogin: false,
    categories: [
      {
        id: 1,
        name: '美食',
        icon: ''
      },
      {
        id: 2,
        name: '超市',
        icon: ''
      },
      {
        id: 3,
        name: '水果',
        icon: ''
      },
      {
        id: 4,
        name: '奶茶',
        icon: ''
      },
      {
        id: 5,
        name: '早餐',
        icon: ''
      },
      {
        id: 6,
        name: '午餐',
        icon: ''
      },
      {
        id: 7,
        name: '晚餐',
        icon: ''
      },
      {
        id: 8,
        name: '夜宵',
        icon: ''
      }
    ],
    shops: [
      {
        id: 1,
        name: '肯德基',
        description: '美味炸鸡，快乐加倍',
        logo: '',
        rating: 4.8,
        distance: 1.2,
        deliveryFee: 5.0
      },
      {
        id: 2,
        name: '麦当劳',
        description: "I'm lovin' it",
        logo: '',
        rating: 4.7,
        distance: 0.8,
        deliveryFee: 4.0
      },
      {
        id: 3,
        name: '星巴克',
        description: '用心做好每一杯咖啡',
        logo: '',
        rating: 4.9,
        distance: 1.5,
        deliveryFee: 6.0
      }
    ],
    loading: false
  },

  onLoad() {
    // 页面加载时的初始化逻辑
    console.log('index.js onLoad被调用');
    // 检查登录状态
    this.checkLoginStatus();
    // 延迟加载数据，避免页面加载时的阻塞
    setTimeout(() => {
      this.loadData();
    }, 100);
  },

  onShow() {
    // 页面显示时的逻辑
    console.log('index.js onShow被调用');
    // 再次检查登录状态
    this.checkLoginStatus();
  },

  onReady() {
    // 页面初次渲染完成时的逻辑
    console.log('index.js onReady被调用');
  },
  
  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    if (token && userInfo) {
      app.globalData.token = token;
      app.globalData.userInfo = userInfo;
      this.setData({
        isLogin: true
      });
    } else {
      this.setData({
        isLogin: false
      });
    }
  },
  
  // 获取用户信息
  onGetUserInfo(e) {
    if (e.detail.userInfo) {
      // 用户同意授权
      this.login(e.detail.userInfo);
    } else {
      // 用户拒绝授权
      wx.showToast({
        title: '授权失败，请重新授权',
        icon: 'none',
        duration: 2000
      });
    }
  },
  
  // 登录
  login(userInfo) {
    wx.login({
      success: (res) => {
        if (res.code) {
          // 调用后端登录API
          post('/auth/wx-login', {
            code: res.code
          }, { showLoading: true }).then((data) => {
            // 登录成功，保存token和用户信息
            const { token, customer } = data;
            wx.setStorageSync('token', token);
            wx.setStorageSync('userInfo', customer);
            app.globalData.token = token;
            app.globalData.userInfo = customer;
            
            this.setData({
              isLogin: true
            });
            
            wx.showToast({
              title: '登录成功',
              icon: 'success',
              duration: 2000
            });
          }).catch((err) => {
            console.error('登录失败:', err);
            wx.showToast({
              title: '登录失败，请重试',
              icon: 'none',
              duration: 2000
            });
          });
        } else {
          console.error('获取code失败:', res.errMsg);
          wx.showToast({
            title: '登录失败，请重试',
            icon: 'none',
            duration: 2000
          });
        }
      },
      fail: (err) => {
        console.error('wx.login失败:', err);
        wx.showToast({
          title: '登录失败，请重试',
          icon: 'none',
          duration: 2000
        });
      }
    });
  },

  // 加载数据
  loadData() {
    console.log('index.js 开始加载数据');
    
    try {
      // 先显示本地数据，确保页面能正常显示
      this.setData({
        loading: true
      });
      
      // 引入request工具
      const { get } = require('../../utils/request');
      
      // 获取分类数据
      console.log('index.js 开始请求分类数据');
      get('/category/list', {}, { showLoading: false }).then((data) => {
        console.log('index.js 获取分类数据成功:', data);
        this.setData({
          categories: data || []
        });
      }).catch((err) => {
        console.error('index.js 加载分类失败:', err);
        // 失败时使用本地数据，确保页面能正常显示
      }).finally(() => {
        console.log('index.js 分类数据请求完成');
      });
      
      // 获取推荐店铺数据
      console.log('index.js 开始请求店铺数据');
      get('/shop/list', {}, { showLoading: false }).then((data) => {
        console.log('index.js 获取店铺数据成功:', data);
        this.setData({
          shops: data || []
        });
      }).catch((err) => {
        console.error('index.js 加载推荐店铺失败:', err);
        // 失败时使用本地数据，确保页面能正常显示
      }).finally(() => {
        console.log('index.js 店铺数据请求完成');
        this.setData({
          loading: false
        });
      });
    } catch (error) {
      console.error('index.js 加载数据过程中发生异常:', error);
      // 发生异常时使用本地数据，确保页面能正常显示
      this.setData({
        loading: false
      });
    }
  },

  // 跳转到店铺列表
  navigateToShop() {
    console.log('index.js 跳转到店铺列表');
    wx.navigateTo({
      url: '/pages/shop/shop',
      fail: (err) => {
        console.error('index.js 跳转到店铺列表失败:', err);
      }
    });
  },

  // 跳转到店铺详情
  navigateToShopDetail(e) {
    console.log('index.js 跳转到店铺详情，参数:', e);
    const shopId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/shop/shop-detail?id=${shopId}`,
      fail: (err) => {
        console.error('index.js 跳转到店铺详情失败:', err);
      }
    });
  }
})