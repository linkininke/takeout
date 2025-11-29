// order.js
const { get } = require('../../utils/request');
Page({
  data: {
    orderList: [],
    statusList: [
      { id: -1, name: '全部' },
      { id: 1, name: '待付款' },
      { id: 2, name: '待接单' },
      { id: 3, name: '待配送' },
      { id: 4, name: '待取餐' },
      { id: 5, name: '已完成' },
      { id: 6, name: '已取消' }
    ],
    currentStatus: -1,
    loading: false,
    page: 1,
    size: 10,
    hasMore: true
  },
  
  onLoad() {
    // 页面加载时执行
    this.loadOrders();
  },
  
  onShow() {
    // 页面显示时执行
    this.setData({
      orderList: [],
      page: 1,
      hasMore: true
    });
    this.loadOrders();
  },
  
  // 加载订单列表
  loadOrders() {
    if (this.data.loading || !this.data.hasMore) return;
    
    this.setData({ loading: true });
    
    const params = {
      status: this.data.currentStatus === -1 ? '' : this.data.currentStatus,
      page: this.data.page,
      size: this.data.size
    };
    
    get('/orders/list', params, { showLoading: false }).then((data) => {
      const newOrders = data.records || [];
      const orderList = this.data.page === 1 ? newOrders : [...this.data.orderList, ...newOrders];
      
      this.setData({
        orderList: orderList,
        hasMore: newOrders.length === this.data.size,
        page: this.data.page + 1
      });
    }).catch((err) => {
      console.error('加载订单列表失败', err);
    }).finally(() => {
      this.setData({ loading: false });
    });
  },
  
  // 切换订单状态
  switchStatus(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      currentStatus: status,
      orderList: [],
      page: 1,
      hasMore: true
    });
    this.loadOrders();
  },
  
  // 跳转到订单详情
  navigateToOrderDetail(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order/order-detail?id=${orderId}`
    });
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      orderList: [],
      page: 1,
      hasMore: true
    });
    this.loadOrders();
    wx.stopPullDownRefresh();
  },
  
  // 上拉加载更多
  onReachBottom() {
    this.loadOrders();
  }
})