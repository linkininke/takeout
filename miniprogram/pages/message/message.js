// pages/message/message.js
const { get, put } = require('../../utils/request');
Page({
  data: {
    messageList: [],
    loading: false,
    page: 1,
    size: 10,
    hasMore: true
  },
  
  onLoad() {
    // 加载消息列表
    this.loadMessageList();
  },
  
  // 加载消息列表
  loadMessageList() {
    if (this.data.loading || !this.data.hasMore) return;
    
    this.setData({ loading: true });
    
    get('/message/list', { page: this.data.page, size: this.data.size }, { showLoading: false }).then((data) => {
      const newMessages = data.records || [];
      const messageList = this.data.page === 1 ? newMessages : [...this.data.messageList, ...newMessages];
      
      this.setData({
        messageList: messageList,
        hasMore: newMessages.length === this.data.size,
        page: this.data.page + 1
      });
    }).catch((err) => {
      console.error('加载消息列表失败', err);
    }).finally(() => {
      this.setData({ loading: false });
    });
  },
  
  // 跳转到消息详情页面
  navigateToMessageDetail(e) {
    const messageId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/message/message-detail?id=${messageId}`
    });
  },
  
  // 标记消息为已读
  markAsRead(e) {
    const messageId = e.currentTarget.dataset.id;
    put(`/message/read`, { id: messageId }, { showLoading: false }).then(() => {
      // 更新消息状态
      const messageList = this.data.messageList.map(item => {
        if (item.id === messageId) {
          return { ...item, readFlag: 1 };
        }
        return item;
      });
      this.setData({
        messageList: messageList
      });
    }).catch((err) => {
      console.error('标记消息为已读失败', err);
    });
  },
  
  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      messageList: [],
      page: 1,
      hasMore: true
    });
    this.loadMessageList();
    wx.stopPullDownRefresh();
  },
  
  // 上拉加载更多
  onReachBottom() {
    this.loadMessageList();
  }
})