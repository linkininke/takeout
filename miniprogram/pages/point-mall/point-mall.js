// pages/point-mall/point-mall.js
const { get, post } = require('../../utils/request');
Page({
  data: {
    point: 0,
    goodsList: [],
    loading: false,
    page: 1,
    size: 10,
    hasMore: true
  },
  
  onLoad() {
    // 加载积分余额
    this.loadPoint();
    // 加载商品列表
    this.loadGoodsList();
  },
  
  // 加载积分余额
  loadPoint() {
    get('/point', {}, { showLoading: true }).then((data) => {
      this.setData({
        point: data
      });
    }).catch((err) => {
      console.error('加载积分余额失败', err);
    });
  },
  
  // 加载商品列表
  loadGoodsList() {
    if (this.data.loading || !this.data.hasMore) return;
    
    this.setData({ loading: true });
    
    get('/point-mall-item/list', { page: this.data.page, size: this.data.size }, { showLoading: false }).then((data) => {
      const newGoods = data.records || [];
      const goodsList = this.data.page === 1 ? newGoods : [...this.data.goodsList, ...newGoods];
      
      this.setData({
        goodsList: goodsList,
        hasMore: newGoods.length === this.data.size,
        page: this.data.page + 1
      });
    }).catch((err) => {
      console.error('加载商品列表失败', err);
    }).finally(() => {
      this.setData({ loading: false });
    });
  },
  
  // 兑换商品
  exchangeGoods(e) {
    const goodsId = e.currentTarget.dataset.id;
    const goods = this.data.goodsList.find(item => item.id === goodsId);
    
    // 检查积分是否足够
    if (this.data.point < goods.point) {
      wx.showToast({
        title: '积分不足',
        icon: 'none'
      });
      return;
    }
    
    // 确认兑换
    wx.showModal({
      title: '确认兑换',
      content: `确定要使用${goods.point}积分兑换${goods.name}吗？`,
      success: (res) => {
        if (res.confirm) {
          // 调用API兑换商品
          post('/point-exchange-record', { pointMallItemId: goodsId }, { showLoading: true }).then(() => {
            wx.showToast({
              title: '兑换成功',
              icon: 'success'
            });
            // 重新加载积分余额和商品列表
            this.loadPoint();
          }).catch((err) => {
            console.error('兑换商品失败', err);
          });
        }
      }
    });
  },
  
  // 跳转到兑换记录页面
  navigateToExchangeRecord() {
    wx.navigateTo({
      url: '/pages/point-mall/exchange-record'
    });
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      goodsList: [],
      page: 1,
      hasMore: true
    });
    this.loadPoint();
    this.loadGoodsList();
    wx.stopPullDownRefresh();
  },
  
  // 上拉加载更多
  onReachBottom() {
    this.loadGoodsList();
  }
})