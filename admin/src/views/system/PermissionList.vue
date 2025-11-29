<template>
  <div class="permission-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>权限管理</h2>
          <el-button type="primary" @click="handleAdd" class="add-button">
            <el-icon><Plus /></el-icon>
            添加权限
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" class="search-form" @submit.prevent>
        <el-form-item label="权限名称">
          <el-input v-model="searchForm.name" placeholder="请输入权限名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="权限类型">
          <el-select v-model="searchForm.type" placeholder="请选择权限类型" clearable>
            <el-option label="菜单" value="1"></el-option>
            <el-option label="按钮" value="2"></el-option>
            <el-option label="接口" value="3"></el-option>
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

      <!-- 权限列表 -->
      <el-table
        v-loading="loading"
        :data="permissionList"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column prop="name" label="权限名称" width="180"></el-table-column>
        <el-table-column prop="code" label="权限编码" width="180"></el-table-column>
        <el-table-column prop="type" label="权限类型" width="120">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)">{{ getTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="请求URL"></el-table-column>
        <el-table-column prop="method" label="请求方法" width="120"></el-table-column>
        <el-table-column prop="sort" label="排序" width="80"></el-table-column>
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="permissionFormRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-position="right"
        label-width="120px"
      >
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="permissionForm.name" placeholder="请输入权限名称"></el-input>
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="permissionForm.code" placeholder="请输入权限编码"></el-input>
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-select v-model="permissionForm.type" placeholder="请选择权限类型">
            <el-option label="菜单" value="1"></el-option>
            <el-option label="按钮" value="2"></el-option>
            <el-option label="接口" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父权限" prop="parentId">
          <el-select v-model="permissionForm.parentId" placeholder="请选择父权限">
            <el-option label="顶级权限" value="0"></el-option>
            <el-option
              v-for="permission in permissionOptions"
              :key="permission.id"
              :label="permission.name"
              :value="permission.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请求URL" prop="url">
          <el-input v-model="permissionForm.url" placeholder="请输入请求URL"></el-input>
        </el-form-item>
        <el-form-item label="请求方法" prop="method">
          <el-select v-model="permissionForm.method" placeholder="请选择请求方法">
            <el-option label="GET" value="GET"></el-option>
            <el-option label="POST" value="POST"></el-option>
            <el-option label="PUT" value="PUT"></el-option>
            <el-option label="DELETE" value="DELETE"></el-option>
            <el-option label="PATCH" value="PATCH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="permissionForm.sort" :min="0" :step="1" placeholder="请输入排序"></el-input-number>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="permissionForm.description"
            type="textarea"
            placeholder="请输入权限描述"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="permissionForm.status"
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, RefreshRight, Edit, Delete } from '@element-plus/icons-vue'
import axios from '../../utils/axios'

// 状态定义
const loading = ref(false)
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const permissionFormRef = ref(null)

// 搜索表单
const searchForm = reactive({
  name: '',
  type: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 权限列表
const permissionList = ref([])

// 权限选项（用于父权限选择）
const permissionOptions = ref([])

// 权限表单
const permissionForm = reactive({
  id: '',
  name: '',
  code: '',
  description: '',
  parentId: 0,
  type: 1,
  url: '',
  method: '',
  sort: 0,
  status: 1
})

// 表单验证规则
const permissionRules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' },
    { min: 2, max: 50, message: '权限名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' },
    { min: 2, max: 50, message: '权限编码长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return permissionForm.id ? '编辑权限' : '添加权限'
})

// 获取权限类型名称
const getTypeName = (type) => {
  switch (type) {
    case 1: return '菜单'
    case 2: return '按钮'
    case 3: return '接口'
    default: return '未知'
  }
}

// 获取权限类型标签样式
const getTypeTag = (type) => {
  switch (type) {
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'info'
    default: return 'default'
  }
}

// 加载权限列表
const loadPermissionList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      name: searchForm.name,
      type: searchForm.type
    }
    const response = await axios.get('/system/permission/page', { params })
    permissionList.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取权限列表失败')
  } finally {
    loading.value = false
  }
}

// 加载权限选项（用于父权限选择）
const loadPermissionOptions = async () => {
  try {
    const response = await axios.get('/system/permission/list')
    permissionOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('获取权限选项失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  loadPermissionList()
}

// 重置
const handleReset = () => {
  searchForm.name = ''
  searchForm.type = ''
  handleSearch()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadPermissionList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadPermissionList()
}

// 添加权限
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑权限
const handleEdit = (row) => {
  permissionForm.id = row.id
  permissionForm.name = row.name
  permissionForm.code = row.code
  permissionForm.description = row.description
  permissionForm.parentId = row.parentId
  permissionForm.type = row.type
  permissionForm.url = row.url
  permissionForm.method = row.method
  permissionForm.sort = row.sort
  permissionForm.status = row.status
  dialogVisible.value = true
}

// 删除权限
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该权限吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/system/permission/${id}`)
      ElMessage.success('删除成功')
      loadPermissionList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变更
const handleStatusChange = (row) => {
  // 这里可以调用API更新权限状态
  console.log('状态变更', row)
}

// 表单提交
const handleSubmit = async () => {
  if (!permissionFormRef.value) return
  
  await permissionFormRef.value.validate(async (valid) => {
    if (valid) {
      dialogLoading.value = true
      try {
        if (permissionForm.id) {
          // 编辑权限
          await axios.put(`/system/permission/${permissionForm.id}`, permissionForm)
          ElMessage.success('编辑成功')
        } else {
          // 添加权限
          await axios.post('/system/permission', permissionForm)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadPermissionList()
      } catch (error) {
        ElMessage.error(permissionForm.id ? '编辑失败' : '添加失败')
      } finally {
        dialogLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  permissionForm.id = ''
  permissionForm.name = ''
  permissionForm.code = ''
  permissionForm.description = ''
  permissionForm.parentId = 0
  permissionForm.type = 1
  permissionForm.url = ''
  permissionForm.method = ''
  permissionForm.sort = 0
  permissionForm.status = 1
  if (permissionFormRef.value) {
    permissionFormRef.value.resetFields()
  }
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 页面挂载时加载数据
onMounted(() => {
  loadPermissionList()
  loadPermissionOptions()
})
</script>

<style scoped>
.permission-list-container {
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