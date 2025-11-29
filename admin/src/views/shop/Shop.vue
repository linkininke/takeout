<template>
  <div class="shop-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>店铺管理</h2>
        </div>
      </template>
      
      <el-form
        ref="shopFormRef"
        :model="shopForm"
        :rules="shopRules"
        label-position="right"
        label-width="120px"
        class="shop-form"
      >
        <el-form-item label="店铺名称" prop="name">
          <el-input v-model="shopForm.name" placeholder="请输入店铺名称"></el-input>
        </el-form-item>
        <el-form-item label="店铺地址" prop="address">
          <el-input v-model="shopForm.address" placeholder="请输入店铺地址"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="shopForm.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="店铺描述" prop="description">
          <el-input
            v-model="shopForm.description"
            type="textarea"
            placeholder="请输入店铺描述"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="店铺Logo" prop="logo">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :on-success="handleLogoUploadSuccess"
            :before-upload="beforeLogoUpload"
            :http-request="customLogoUpload"
          >
            <img v-if="shopForm.logo" :src="shopForm.logo" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="营业时间" prop="openingHours">
          <el-input v-model="shopForm.openingHours" placeholder="请输入营业时间，例如：09:00-22:00"></el-input>
        </el-form-item>
        <el-form-item label="配送费" prop="deliveryFee">
          <el-input-number
            v-model="shopForm.deliveryFee"
            :min="0"
            :step="0.5"
            :precision="1"
            placeholder="请输入配送费"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="预计配送时间" prop="deliveryTime">
          <el-input-number
            v-model="shopForm.deliveryTime"
            :min="15"
            :step="5"
            placeholder="请输入预计配送时间(分钟)"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="起送金额" prop="minOrderAmount">
          <el-input-number
            v-model="shopForm.minOrderAmount"
            :min="0"
            :step="1"
            :precision="1"
            placeholder="请输入起送金额"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="店铺状态" prop="status">
          <div class="status-switch-container">
            <el-switch
              v-model="shopForm.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange"
              active-text="营业中"
              inactive-text="休息中"
              :active-color="'#13ce66'"
              :inactive-color="'#ff4d4f'"
            ></el-switch>
            <span class="status-text">{{ shopForm.status === 1 ? '店铺当前处于营业状态' : '店铺当前处于休息状态' }}</span>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit" class="submit-button">
            <el-icon><Check /></el-icon>
            保存
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Check, RefreshRight } from '@element-plus/icons-vue'
import axios from '../../utils/axios'

// 状态定义
const loading = ref(false)
const shopFormRef = ref(null)

// 店铺表单
const shopForm = reactive({
  id: '',
  name: '',
  address: '',
  phone: '',
  description: '',
  logo: '',
  openingHours: '',
  deliveryFee: 0,
  deliveryTime: 30,
  minOrderAmount: 0,
  status: 1
})

// 表单验证规则
const shopRules = {
  name: [
    { required: true, message: '请输入店铺名称', trigger: 'blur' },
    { min: 2, max: 50, message: '店铺名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入店铺地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  openingHours: [
    { required: true, message: '请输入营业时间', trigger: 'blur' }
  ]
}

// 加载店铺信息
const loadShopInfo = async () => {
  loading.value = true
  try {
    const response = await axios.get('/shop')
    const shop = response.data || response
    Object.assign(shopForm, shop)
    // 确保id是数字类型
    shopForm.id = Number(shopForm.id)
  } catch (error) {
    console.error('获取店铺信息失败:', error)
    ElMessage.error('获取店铺信息失败')
  } finally {
    loading.value = false
  }
}

// 表单提交
const handleSubmit = async () => {
  if (!shopFormRef.value) return
  
  await shopFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await axios.put('/shop', shopForm)
        ElMessage.success('保存成功')
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 重置表单
const handleReset = () => {
  loadShopInfo()
}

// 状态变更
const handleStatusChange = () => {
  const statusText = shopForm.status === 1 ? '营业' : '休息'
  ElMessageBox.confirm(`确定要将店铺状态设置为${statusText}吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 确保id是数字类型
      const shopId = Number(shopForm.id)
      await axios.put(`/shop/status?id=${shopId}&status=${shopForm.status}`)
      ElMessage.success(`店铺已${statusText}`)
    } catch (error) {
      // 恢复原状态
      shopForm.status = shopForm.status === 1 ? 0 : 1
      ElMessage.error('状态更新失败')
    }
  }).catch(() => {
    // 恢复原状态
    shopForm.status = shopForm.status === 1 ? 0 : 1
  })
}

// Logo上传前验证
const beforeLogoUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// Logo上传成功处理
const handleLogoUploadSuccess = (response) => {
  // 这里处理上传成功后的逻辑
  console.log('Logo上传成功', response)
}

// 自定义Logo上传
const customLogoUpload = (options) => {
  // 这里实现自定义上传逻辑，调用API上传图片
  console.log('自定义Logo上传', options)
  // 模拟上传成功
  setTimeout(() => {
    options.onSuccess({ url: 'https://picsum.photos/200/200' })
    shopForm.logo = 'https://picsum.photos/200/200'
  }, 1000)
}

// 页面挂载时加载数据
onMounted(() => {
  loadShopInfo()
})
</script>

<style scoped>
.shop-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.shop-form {
  max-width: 600px;
}

.avatar-uploader {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  object-fit: cover;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
}

.submit-button {
  margin-right: 10px;
}

.status-switch-container {
  display: flex;
  align-items: center;
  gap: 16px;
}

.status-text {
  font-size: 14px;
  color: #606266;
  margin-top: 4px;
}
</style>