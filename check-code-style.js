const fs = require('fs');
const path = require('path');

// 检查单个文件的代码规范
function checkFile(filePath) {
  console.log(`=== ${path.relative('e:/mycode/takeout', filePath)} 代码规范检查 ===`);
  
  try {
    const content = fs.readFileSync(filePath, 'utf8');
    const lines = content.split('\n');
    
    // 统计信息
    console.log(`文件长度: ${content.length} 字符`);
    console.log(`行数: ${lines.length} 行`);
    
    // 检查注释
    const hasComment = content.includes('//') || content.includes('/*');
    console.log(`是否有注释: ${hasComment}`);
    
    // 检查缩进（使用2个空格）
    let indentIssue = 0;
    lines.forEach(line => {
      const trimmed = line.trimStart();
      if (trimmed && line.startsWith('   ')) {
        indentIssue++;
      }
    });
    console.log(`缩进问题数: ${indentIssue}`);
    
    // 检查驼峰命名
    let camelCaseIssue = 0;
    const camelCaseRegex = /(function\s+|const\s+|let\s+|var\s+)([a-zA-Z0-9_]+)/g;
    let match;
    while ((match = camelCaseRegex.exec(content)) !== null) {
      const name = match[2];
      if (name.includes('_') && !name.startsWith('_') && !name.endsWith('_')) {
        camelCaseIssue++;
      }
    }
    console.log(`驼峰命名问题数: ${camelCaseIssue}`);
    
    console.log('检查完成\n');
    
  } catch (err) {
    console.error(`读取文件失败: ${err.message}\n`);
  }
}

// 检查关键文件
const filesToCheck = [
  'miniprogram/utils/request.js',
  'miniprogram/app.js',
  'miniprogram/pages/index/index.js',
  'miniprogram/pages/shop/shop.js',
  'miniprogram/pages/cart/cart.js'
];

filesToCheck.forEach(file => {
  const fullPath = path.join('e:/mycode/takeout', file);
  checkFile(fullPath);
});
