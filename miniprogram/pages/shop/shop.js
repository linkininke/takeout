// pages/shop/shop.js
const { get } = require('../../utils/request');
Page({
  data: {
    shops: [],
    categories: [],
    currentCategory: 0,
    loading: false,
    page: 1,
    size: 10,
    hasMore: true,
    keyword: ''
  },

  onLoad() {
    // 加载分类和店铺数据
    this.loadCategories();
    this.loadShops();
  },

  // 加载分类数据
  loadCategories() {
    // 调用API获取分类数据
    get('/category/list', {}, { showLoading: true }).then((data) => {
      this.setData({
        categories: [{ id: 0, name: '全部' }, ...data]
      });
    }).catch((err) => {
      console.error('加载分类失败', err);
    });
  },

  // 加载店铺数据
  loadShops() {
    if (this.data.loading || !this.data.hasMore) return;

    this.setData({ loading: true });

    // 调用API获取店铺数据
    const params = {
      categoryId: this.data.currentCategory === 0 ? '' : this.data.currentCategory,
      keyword: this.data.keyword,
      page: this.data.page,
      size: this.data.size
    };

    get('/shop/list', params, { showLoading: false }).then((data) => {
      const newShops = data.records || [];
      const shops = this.data.page === 1 ? newShops : [...this.data.shops, ...newShops];
      
      this.setData({
        shops: shops,
        hasMore: newShops.length === this.data.size,
        page: this.data.page + 1
      });
    }).catch((err) => {
      console.error('加载店铺失败', err);
    }).finally(() => {
      this.setData({ loading: false });
    });
  },

  // 切换分类
  switchCategory(e) {
    const categoryId = e.currentTarget.dataset.id;
    this.setData({
      currentCategory: categoryId,
      shops: [],
      page: 1,
      hasMore: true
    });
    this.loadShops();
  },

  // 跳转到店铺详情
  navigateToShopDetail(e) {
    const shopId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/shop/shop-detail?id=${shopId}`
    });
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      shops: [],
      page: 1,
      hasMore: true
    });
    this.loadShops();
    wx.stopPullDownRefresh();
  },

  // 输入框输入事件
  onKeywordInput(e) {
    this.setData({
      keyword: e.detail.value
    });
  },
  
  // 搜索事件
  onSearch() {
    this.setData({
      shops: [],
      page: 1,
      hasMore: true
    });
    this.loadShops();
  },
  
  // 上拉加载更多
  onReachBottom() {
    this.loadShops();
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      shops: [],
      page: 1,
      hasMore: true
    });
    this.loadShops();
    wx.stopPullDownRefresh();
  }
});
