// API配置文件
const config = {
  // 开发环境配置
  development: {
    // 已替换为电脑实际IP地址，真机测试时确保手机和电脑在同一网络
    baseURL: 'http://192.168.1.8:8082/api'
  },
  // 生产环境配置
  production: {
    baseURL: 'http://192.168.1.8:8082/api'
  }
};

// 根据当前环境选择配置
const env = wx.getAccountInfoSync().miniProgram.envVersion;
let baseURL = config.development.baseURL;

if (env === 'release') {
  baseURL = config.production.baseURL;
} else if (env === 'trial') {
  baseURL = config.development.baseURL; // 体验版也使用开发环境
}

console.log('当前环境:', env, '使用的baseURL:', baseURL);

module.exports = {
  baseURL
};
