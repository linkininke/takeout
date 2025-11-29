// pages/address/edit-address.js
const { get, put } = require('../../utils/request');
Page({
  data: {
    addressId: '',
    name: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    detail: '',
    isDefault: false
  },
  
  onLoad(options) {
    // 获取地址ID
    const addressId = options.id;
    this.setData({
      addressId: addressId
    });
    // 加载地址详情
    this.loadAddressDetail();
  },
  
  // 加载地址详情
  loadAddressDetail() {
    get(`/address-book/${this.data.addressId}`, {}, { showLoading: true }).then((data) => {
      this.setData({
        name: data.name,
        phone: data.phone,
        province: data.province,
        city: data.city,
        district: data.district,
        detail: data.detail,
        isDefault: data.isDefault
      });
    }).catch((err) => {
      console.error('加载地址详情失败', err);
      wx.navigateBack();
    });
  },
  
  // 输入框输入事件
  onInput(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({
      [field]: e.detail.value
    });
  },
  
  // 切换默认地址
  toggleDefault() {
    this.setData({
      isDefault: !this.data.isDefault
    });
  },
  
  // 提交表单
  submitForm() {
    // 表单验证
    if (!this.data.name) {
      wx.showToast({
        title: '请输入收货人姓名',
        icon: 'none'
      });
      return;
    }
    
    if (!this.data.phone) {
      wx.showToast({
        title: '请输入手机号码',
        icon: 'none'
      });
      return;
    }
    
    // 手机号码验证
    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(this.data.phone)) {
      wx.showToast({
        title: '请输入正确的手机号码',
        icon: 'none'
      });
      return;
    }
    
    if (!this.data.province || !this.data.city || !this.data.district) {
      wx.showToast({
        title: '请选择省市区',
        icon: 'none'
      });
      return;
    }
    
    if (!this.data.detail) {
      wx.showToast({
        title: '请输入详细地址',
        icon: 'none'
      });
      return;
    }
    
    // 构建地址数据
    const addressData = {
      id: this.data.addressId,
      name: this.data.name,
      phone: this.data.phone,
      province: this.data.province,
      city: this.data.city,
      district: this.data.district,
      detail: this.data.detail,
      isDefault: this.data.isDefault
    };
    
    // 调用API更新地址
    put('/address-book', addressData, { showLoading: true }).then(() => {
      wx.showToast({
        title: '更新成功',
        icon: 'success'
      });
      // 返回上一页
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }).catch((err) => {
      console.error('更新地址失败', err);
    });
  }
})
