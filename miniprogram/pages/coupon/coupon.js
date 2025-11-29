// pages/coupon/coupon.js
const { get, post } = require('../../utils/request');
Page({
  data: {
    couponList: [],
    tabList: [
      { id: 1, name: '可领取' },
      { id: 2, name: '已拥有' },
      { id: 3, name: '已使用' },
      { id: 4, name: '已过期' }
    ],
    currentTab: 1,
    loading: false,
    page: 1,
    size: 10,
    hasMore: true
  },
  
  onLoad() {
    // 加载优惠券列表
    this.loadCoupons();
  },
  
  // 切换标签
  switchTab(e) {
    const tabId = e.currentTarget.dataset.id;
    this.setData({
      currentTab: tabId,
      couponList: [],
      page: 1,
      hasMore: true
    });
    this.loadCoupons();
  },
  
  // 加载优惠券列表
  loadCoupons() {
    if (this.data.loading || !this.data.hasMore) return;
    
    this.setData({ loading: true });
    
    let url = '';
    if (this.data.currentTab === 1) {
      // 可领取优惠券
      url = '/coupon/list';
    } else if (this.data.currentTab === 2) {
      // 已拥有优惠券
      url = '/coupon-record/list';
    } else if (this.data.currentTab === 3) {
      // 已使用优惠券
      url = '/coupon-record/list?status=1';
    } else if (this.data.currentTab === 4) {
      // 已过期优惠券
      url = '/coupon-record/list?status=2';
    }
    
    get(url, { page: this.data.page, size: this.data.size }, { showLoading: false }).then((data) => {
      const newCoupons = data.records || [];
      const couponList = this.data.page === 1 ? newCoupons : [...this.data.couponList, ...newCoupons];
      
      this.setData({
        couponList: couponList,
        hasMore: newCoupons.length === this.data.size,
        page: this.data.page + 1
      });
    }).catch((err) => {
      console.error('加载优惠券列表失败', err);
    }).finally(() => {
      this.setData({ loading: false });
    });
  },
  
  // 领取优惠券
  receiveCoupon(e) {
    const couponId = e.currentTarget.dataset.id;
    post(`/coupon-record/receive`, { couponId: couponId }, { showLoading: true }).then(() => {
      wx.showToast({
        title: '领取成功',
        icon: 'success'
      });
      // 重新加载优惠券列表
      this.setData({
        couponList: [],
        page: 1,
        hasMore: true
      });
      this.loadCoupons();
    }).catch((err) => {
      console.error('领取优惠券失败', err);
    });
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      couponList: [],
      page: 1,
      hasMore: true
    });
    this.loadCoupons();
    wx.stopPullDownRefresh();
  },
  
  // 上拉加载更多
  onReachBottom() {
    this.loadCoupons();
  }
})