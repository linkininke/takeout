// pages/address/address.js
const { get, post, put, delete: del } = require('../../utils/request');
Page({
  data: {
    addressList: [],
    selectedAddressId: '',
    isSelectMode: false
  },
  
  onLoad(options) {
    // 检查是否为选择地址模式
    if (options.isSelectMode) {
      this.setData({
        isSelectMode: true
      });
    }
    // 加载地址列表
    this.loadAddressList();
  },
  
  onShow() {
    // 页面显示时重新加载地址列表
    this.loadAddressList();
  },
  
  // 加载地址列表
  loadAddressList() {
    get('/address-book/list', {}, { showLoading: true }).then((data) => {
      this.setData({
        addressList: data
      });
    }).catch((err) => {
      console.error('加载地址列表失败', err);
    });
  },
  
  // 跳转到添加地址页面
  navigateToAddAddress() {
    wx.navigateTo({
      url: '/pages/address/add-address'
    });
  },
  
  // 跳转到编辑地址页面
  navigateToEditAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/address/edit-address?id=${addressId}`
    });
  },
  
  // 设置默认地址
  setDefaultAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    put(`/address-book/default`, { id: addressId }, { showLoading: true }).then(() => {
      wx.showToast({
        title: '设置成功',
        icon: 'success'
      });
      // 重新加载地址列表
      this.loadAddressList();
    }).catch((err) => {
      console.error('设置默认地址失败', err);
    });
  },
  
  // 删除地址
  deleteAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '确定要删除该地址吗？',
      success: (res) => {
        if (res.confirm) {
          del(`/address-book/${addressId}`, {}, { showLoading: true }).then(() => {
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            // 重新加载地址列表
            this.loadAddressList();
          }).catch((err) => {
            console.error('删除地址失败', err);
          });
        }
      }
    });
  },
  
  // 选择地址（用于订单确认页面）
  selectAddress(e) {
    if (this.data.isSelectMode) {
      const addressId = e.currentTarget.dataset.id;
      const selectedAddress = this.data.addressList.find(address => address.id === addressId);
      // 返回上一页并传递选择的地址
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      prevPage.setData({
        selectedAddress: selectedAddress
      });
      wx.navigateBack();
    }
  }
})
