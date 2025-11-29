# API配置说明

## 问题分析

微信小程序在开发者工具中可以正常显示，但在真机测试时显示不出来，主要原因是：

1. **API地址配置问题**：代码中使用了`http://localhost:8082/api`作为API地址
2. **localhost限制**：`localhost`只能在本地环境访问，真机无法通过localhost访问电脑上的后端服务
3. **网络环境差异**：开发者工具和真机处于不同的网络环境

## 解决方案

1. **创建了统一的API配置文件**：`config/api.js`
2. **支持多环境配置**：开发环境和生产环境
3. **支持自动环境检测**：根据小程序运行环境自动切换配置

## 配置步骤

### 1. 查看电脑IP地址

在命令行中执行以下命令查看电脑的IP地址：

- **Windows**：`ipconfig`，找到"IPv4地址"
- **Mac/Linux**：`ifconfig`，找到"inet"地址

### 2. 修改API配置

编辑`config/api.js`文件，将`development.baseURL`中的`localhost`替换为你的电脑IP地址：

```javascript
const config = {
  // 开发环境配置
  development: {
    // 将localhost替换为你的电脑IP地址
    baseURL: 'http://192.168.1.8:8082/api',
  },
  // 生产环境配置
  production: {
    baseURL: 'https://your-domain.com/api'
  }
};
```

### 3. 确保手机和电脑在同一网络

真机测试时，确保手机和电脑连接到**同一个WiFi网络**，否则真机无法访问电脑上的后端服务。

## 配置说明

### 环境自动切换

- **开发版**：使用`development`配置
- **体验版**：使用`production`配置
- **正式版**：使用`production`配置

### API请求示例

在页面中使用配置的API地址：

```javascript
const { baseURL } = require('../../config/api');

wx.request({
  url: `${baseURL}/shop/list`,
  method: 'GET',
  success: (res) => {
    // 处理响应数据
  }
});
```

## 注意事项

1. **不要硬编码API地址**：所有API请求都应该使用`baseURL`配置
2. **定期检查IP地址**：如果你的电脑IP地址变化，需要重新配置
3. **生产环境配置**：上线前请将`production.baseURL`替换为正式的域名
4. **HTTPS要求**：生产环境必须使用HTTPS协议

## 常见问题

### Q: 为什么真机还是无法访问API？

A: 请检查以下几点：
1. 电脑IP地址是否正确配置
2. 手机和电脑是否在同一WiFi网络
3. 后端服务是否正在运行
4. 防火墙是否允许8082端口访问

### Q: 如何查看API请求日志？

A: 在微信开发者工具中，打开"调试器" -> "Network"标签，可以查看所有API请求和响应。

### Q: 如何处理跨域问题？

A: 后端服务需要配置CORS（跨域资源共享），允许小程序域名访问。

## 总结

通过统一的API配置文件，可以轻松解决微信小程序真机测试时无法访问API的问题。只需要简单配置电脑IP地址，确保手机和电脑在同一网络，就可以正常进行真机测试了。
