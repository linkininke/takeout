// 统一网络请求封装
const apiConfig = require('../config/api');
const app = getApp();

/**
 * 封装微信请求
 * @param {Object} options - 请求配置
 * @param {string} options.url - 请求URL
 * @param {string} [options.method='GET'] - 请求方法
 * @param {Object} [options.data={}] - 请求数据
 * @param {Object} [options.header={}] - 请求头
 * @param {boolean} [options.showLoading=true] - 是否显示加载提示
 * @returns {Promise} - 返回Promise对象
 */
const request = (options) => {
  const {
    url,
    method = 'GET',
    data = {},
    header = {},
    showLoading = true
  } = options;

  // 显示加载提示
  if (showLoading) {
    wx.showLoading({
      title: '加载中...',
      mask: true
    });
  }

  // 获取token
  const token = app.globalData.token || wx.getStorageSync('token');

  // 默认请求头
  const defaultHeader = {
    'content-type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {})
  };

  return new Promise((resolve, reject) => {
    wx.request({
      url: `${apiConfig.baseURL}${url}`,
      method,
      data,
      header: { ...defaultHeader, ...header },
      timeout: 10000,
      success: (res) => {
        // 隐藏加载提示
        if (showLoading) {
          wx.hideLoading();
        }

        const { code, data: responseData, message } = res.data;

        if (code === 1) {
          // 请求成功
          resolve(responseData);
        } else if (code === 401) {
          // 未授权，清除token并跳转到登录页
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          app.globalData.token = null;
          app.globalData.userInfo = null;
          wx.showToast({
            title: '请先登录',
            icon: 'none',
            duration: 2000
          });
          reject(new Error('未授权'));
        } else {
          // 请求失败
          wx.showToast({
            title: message || '请求失败',
            icon: 'none',
            duration: 2000
          });
          reject(new Error(message || '请求失败'));
        }
      },
      fail: (err) => {
        // 隐藏加载提示
        if (showLoading) {
          wx.hideLoading();
        }

        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
          duration: 2000
        });
        reject(err);
      }
    });
  });
};

// GET请求封装
const get = (url, data = {}, options = {}) => {
  return request({ ...options, url, method: 'GET', data });
};

// POST请求封装
const post = (url, data = {}, options = {}) => {
  return request({ ...options, url, method: 'POST', data });
};

// PUT请求封装
const put = (url, data = {}, options = {}) => {
  return request({ ...options, url, method: 'PUT', data });
};

// DELETE请求封装
const del = (url, data = {}, options = {}) => {
  return request({ ...options, url, method: 'DELETE', data });
};

module.exports = {
  request,
  get,
  post,
  put,
  delete: del
};
