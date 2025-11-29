<template>
  <div class="role-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>角色管理</h2>
          <el-button type="primary" @click="handleAdd" class="add-button">
            <el-icon><Plus /></el-icon>
            添加角色
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" class="search-form" @submit.prevent>
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.name" placeholder="请输入角色名称" clearable></el-input>
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

      <!-- 角色列表 -->
      <el-table
        v-loading="loading"
        :data="roleList"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column prop="name" label="角色名称" width="180"></el-table-column>
        <el-table-column prop="description" label="角色描述"></el-table-column>
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
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="success" size="small" @click="handleAssignPermission(scope.row)">
              <el-icon><Setting /></el-icon>
              分配权限
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
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-position="right"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            placeholder="请输入角色描述"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="roleForm.status"
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

    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="600px"
      @close="handlePermissionDialogClose"
    >
      <el-tree
        v-if="permissionTree.length > 0"
        ref="permissionTreeRef"
        :data="permissionTree"
        show-checkbox
        node-key="id"
        :default-expanded-keys="expandedKeys"
        :default-checked-keys="checkedKeys"
        :props="treeProps"
        class="permission-tree"
      ></el-tree>
      <div v-else class="no-permission">暂无权限数据</div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="permissionDialogLoading" @click="handlePermissionSubmit">
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
import { Plus, Search, RefreshRight, Edit, Setting, Delete } from '@element-plus/icons-vue'
import axios from '../../utils/axios'

// 状态定义
const loading = ref(false)
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const roleFormRef = ref(null)
const permissionDialogVisible = ref(false)
const permissionDialogLoading = ref(false)
const permissionTreeRef = ref(null)

// 搜索表单
const searchForm = reactive({
  name: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 角色列表
const roleList = ref([])

// 角色表单
const roleForm = reactive({
  id: '',
  name: '',
  description: '',
  status: 1
})

// 表单验证规则
const roleRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return roleForm.id ? '编辑角色' : '添加角色'
})

// 权限树数据
const permissionTree = ref([])

// 权限树配置
const treeProps = {
  label: 'name',
  children: 'children'
}

// 展开的节点
const expandedKeys = ref([])

// 选中的节点
const checkedKeys = ref([])

// 当前选中的角色
const currentRole = ref(null)

// 加载角色列表
const loadRoleList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      name: searchForm.name
    }
    const response = await axios.get('/system/role/page', { params })
    roleList.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  loadRoleList()
}

// 重置
const handleReset = () => {
  searchForm.name = ''
  handleSearch()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadRoleList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadRoleList()
}

// 添加角色
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = (row) => {
  roleForm.id = row.id
  roleForm.name = row.name
  roleForm.description = row.description
  roleForm.status = row.status
  dialogVisible.value = true
}

// 删除角色
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/system/role/${id}`)
      ElMessage.success('删除成功')
      loadRoleList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变更
const handleStatusChange = (row) => {
  // 这里可以调用API更新角色状态
  console.log('状态变更', row)
}

// 表单提交
const handleSubmit = async () => {
  if (!roleFormRef.value) return
  
  await roleFormRef.value.validate(async (valid) => {
    if (valid) {
      dialogLoading.value = true
      try {
        if (roleForm.id) {
          // 编辑角色
          await axios.put(`/system/role/${roleForm.id}`, roleForm)
          ElMessage.success('编辑成功')
        } else {
          // 添加角色
          await axios.post('/system/role', roleForm)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadRoleList()
      } catch (error) {
        ElMessage.error(roleForm.id ? '编辑失败' : '添加失败')
      } finally {
        dialogLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  roleForm.id = ''
  roleForm.name = ''
  roleForm.description = ''
  roleForm.status = 1
  if (roleFormRef.value) {
    roleFormRef.value.resetFields()
  }
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 分配权限
const handleAssignPermission = (row) => {
  currentRole.value = row
  loadPermissionTree()
  permissionDialogVisible.value = true
}

// 加载权限树
const loadPermissionTree = async () => {
  try {
    // 加载所有权限
    const response = await axios.get('/system/permission/tree')
    permissionTree.value = response.data || []
    
    // 加载角色已有的权限
    if (currentRole.value) {
      const rolePermissionResponse = await axios.get(`/system/role/${currentRole.value.id}/permissions`)
      const permissions = rolePermissionResponse.data || []
      checkedKeys.value = permissions.map(p => p.id)
      
      // 展开所有节点
      expandedKeys.value = getAllKeys(permissionTree.value)
    }
  } catch (error) {
    ElMessage.error('加载权限树失败')
  }
}

// 获取所有节点ID
const getAllKeys = (tree) => {
  const keys = []
  const traverse = (nodes) => {
    nodes.forEach(node => {
      keys.push(node.id)
      if (node.children && node.children.length > 0) {
        traverse(node.children)
      }
    })
  }
  traverse(tree)
  return keys
}

// 提交权限分配
const handlePermissionSubmit = async () => {
  if (!currentRole.value || !permissionTreeRef.value) return
  
  permissionDialogLoading.value = true
  try {
    const checkedNodes = permissionTreeRef.value.getCheckedNodes(false, true)
    const permissionIds = checkedNodes.map(node => node.id)
    
    await axios.post(`/system/role/${currentRole.value.id}/permissions`, permissionIds)
    ElMessage.success('权限分配成功')
    permissionDialogVisible.value = false
  } catch (error) {
    ElMessage.error('权限分配失败')
  } finally {
    permissionDialogLoading.value = false
  }
}

// 关闭权限分配对话框
const handlePermissionDialogClose = () => {
  currentRole.value = null
  checkedKeys.value = []
  expandedKeys.value = []
}

// 页面挂载时加载数据
onMounted(() => {
  loadRoleList()
})
</script>

<style scoped>
.role-list-container {
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

.permission-tree {
  max-height: 400px;
  overflow-y: auto;
}

.no-permission {
  text-align: center;
  padding: 40px 0;
  color: #999;
}
</style>