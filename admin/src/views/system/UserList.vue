<template>
  <div class="user-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>用户管理</h2>
          <el-button type="primary" @click="handleAdd" class="add-button">
            <el-icon><Plus /></el-icon>
            添加用户
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" class="search-form" @submit.prevent>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.roleId" placeholder="请选择角色" clearable>
            <el-option
              v-for="role in roles"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 用户列表 -->
      <el-table
        v-loading="loading"
        :data="userList"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="180"></el-table-column>
        <el-table-column prop="name" label="姓名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="180"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
        <el-table-column prop="roleId" label="角色" width="120">
          <template #default="scope">
            {{ getRoleName(scope.row.roleId) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="1"
              inactive-value="0"
              @change="handleStatusChange(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="200"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-position="right"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="userForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="userForm.roleId" placeholder="请选择角色">
            <el-option
              v-for="role in roles"
              :key="role.id"
              :label="role.name"
              :value="role.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="userForm.status"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="dialogLoading" @click="handleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Search, RefreshRight, Edit, Delete } from '@element-plus/icons-vue'
import axios from '../../utils/axios'

// 状态定义
const loading = ref(false)
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const userFormRef = ref(null)

// 搜索表单
const searchForm = reactive({
  username: '',
  name: '',
  roleId: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 用户列表
const userList = ref([])

// 角色列表
const roles = ref([
  { id: 1, name: '管理员' },
  { id: 2, name: '商家' }
])

// 用户表单
const userForm = reactive({
  id: '',
  username: '',
  password: '',
  name: '',
  phone: '',
  email: '',
  roleId: '',
  status: 1
})

// 表单验证规则
const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return userForm.id ? '编辑用户' : '添加用户'
})

// 获取角色名称
const getRoleName = (roleId) => {
  const role = roles.value.find(r => r.id === roleId)
  return role ? role.name : ''
}

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      username: searchForm.username,
      name: searchForm.name,
      roleId: searchForm.roleId
    }
    const response = await axios.get('/system/user/page', { params })
    userList.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  loadUserList()
}

// 重置
const handleReset = () => {
  searchForm.username = ''
  searchForm.name = ''
  searchForm.roleId = ''
  handleSearch()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadUserList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadUserList()
}

// 添加用户
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  userForm.id = row.id
  userForm.username = row.username
  userForm.name = row.name
  userForm.phone = row.phone
  userForm.email = row.email
  userForm.roleId = row.roleId
  userForm.status = row.status
  dialogVisible.value = true
}

// 删除用户
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/system/user/${id}`)
      ElMessage.success('删除成功')
      loadUserList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变更
const handleStatusChange = (row) => {
  // 这里可以调用API更新用户状态
  console.log('状态变更', row)
}

// 表单提交
const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      dialogLoading.value = true
      try {
        if (userForm.id) {
          // 编辑用户
          await axios.put(`/system/user/${userForm.id}`, userForm)
          ElMessage.success('编辑成功')
        } else {
          // 添加用户
          await axios.post('/system/user', userForm)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadUserList()
      } catch (error) {
        ElMessage.error(userForm.id ? '编辑失败' : '添加失败')
      } finally {
        dialogLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  userForm.id = ''
  userForm.username = ''
  userForm.password = ''
  userForm.name = ''
  userForm.phone = ''
  userForm.email = ''
  userForm.roleId = ''
  userForm.status = 1
  if (userFormRef.value) {
    userFormRef.value.resetFields()
  }
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 页面挂载时加载数据
onMounted(() => {
  loadUserList()
})
</script>

<style scoped>
.user-list-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.add-button {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>