<template>
  <div class="password-container">
    <h2 class="password-title">修改密码</h2>
    <el-form
      ref="passwordFormRef"
      :model="passwordForm"
      :rules="passwordRules"
      label-position="top"
      class="password-form"
    >
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input
          v-model="passwordForm.oldPassword"
          type="password"
          placeholder="请输入旧密码"
          prefix-icon="Lock"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="passwordForm.newPassword"
          type="password"
          placeholder="请输入新密码（至少8位，包含字母和数字）"
          prefix-icon="Lock"
          show-password
          @input="checkPasswordStrength"
        ></el-input>
        <div class="password-strength" v-if="passwordForm.newPassword">
          <span :class="['strength-item', getStrengthClass(1)]"></span>
          <span :class="['strength-item', getStrengthClass(2)]"></span>
          <span :class="['strength-item', getStrengthClass(3)]"></span>
          <span class="strength-text">{{ strengthText }}</span>
        </div>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input
          v-model="passwordForm.confirmPassword"
          type="password"
          placeholder="请确认新密码"
          prefix-icon="Lock"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleUpdatePassword"
          class="update-button"
        >
          确认修改
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const passwordFormRef = ref()
const loading = ref(false)
const passwordStrength = ref(0)

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, message: '新密码长度不能少于8位', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d)[^\s]{8,}$/, message: '新密码必须包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: confirmPasswordValidator, message: '两次输入密码不一致', trigger: 'blur' }
  ]
}

const confirmPasswordValidator = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const checkPasswordStrength = () => {
  const password = passwordForm.newPassword
  let strength = 0
  
  if (password.length >= 8) strength++
  if (/[a-zA-Z]/.test(password) && /\d/.test(password)) strength++
  if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) strength++
  
  passwordStrength.value = strength
}

const strengthText = computed(() => {
  switch (passwordStrength.value) {
    case 0: return '密码强度：弱'
    case 1: return '密码强度：弱'
    case 2: return '密码强度：中'
    case 3: return '密码强度：强'
    default: return ''
  }
})

const getStrengthClass = (level) => {
  if (passwordStrength.value >= level) {
    return level === 1 ? 'weak' : level === 2 ? 'medium' : 'strong'
  }
  return ''
}

const handleUpdatePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await axios.put('/user/password', passwordForm)
        ElMessage.success('密码修改成功')
        // 重置表单
        passwordForm.oldPassword = ''
        passwordForm.newPassword = ''
        passwordForm.confirmPassword = ''
        passwordStrength.value = 0
      } catch (error) {
        console.error('密码修改失败:', error)
        ElMessage.error(error.response?.data?.message || '密码修改失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.password-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.password-title {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.password-form {
  width: 100%;
}

.update-button {
  width: 100%;
}

.password-strength {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.strength-item {
  display: inline-block;
  width: 60px;
  height: 6px;
  margin-right: 10px;
  background-color: #e0e0e0;
  border-radius: 3px;
  transition: all 0.3s;
}

.strength-item.weak {
  background-color: #ff4d4f;
}

.strength-item.medium {
  background-color: #faad14;
}

.strength-item.strong {
  background-color: #52c41a;
}

.strength-text {
  font-size: 12px;
  color: #606266;
}
</style>