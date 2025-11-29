<template>
  <div class="category-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>菜品分类管理</h2>
          <el-button type="primary" @click="handleAdd" class="add-button">
            <el-icon><Plus /></el-icon>
            添加分类
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" class="search-form" @submit.prevent>
        <el-form-item label="分类名称">
          <el-input v-model="searchForm.name" placeholder="请输入分类名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类类型">
          <el-select v-model="searchForm.type" placeholder="请选择分类类型" clearable>
            <el-option label="菜品分类" value="1"></el-option>
            <el-option label="套餐分类" value="2"></el-option>
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

      <!-- 分类列表 -->
      <el-table
        v-loading="loading"
        :data="categoryList"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column prop="name" label="分类名称" width="180"></el-table-column>
        <el-table-column prop="type" label="分类类型" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.type === 1 ? 'success' : 'warning'">
              {{ scope.row.type === 1 ? '菜品分类' : '套餐分类' }}
            </el-tag>
          </template>
        </el-table-column>
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
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="categoryFormRef"
        :model="categoryForm"
        :rules="categoryRules"
        label-position="right"
        label-width="100px"
      >
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        <el-form-item label="分类类型" prop="type">
          <el-select v-model="categoryForm.type" placeholder="请选择分类类型">
            <el-option label="菜品分类" value="1"></el-option>
            <el-option label="套餐分类" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="categoryForm.sort" :min="0" :step="1" placeholder="请输入排序"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="categoryForm.status"
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
const categoryFormRef = ref(null)

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

// 分类列表
const categoryList = ref([])

// 分类表单
const categoryForm = reactive({
  id: '',
  name: '',
  type: 1,
  sort: 0,
  status: 1
})

// 表单验证规则
const categoryRules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 20, message: '分类名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择分类类型', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return categoryForm.id ? '编辑分类' : '添加分类'
})

// 加载分类列表
const loadCategoryList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      name: searchForm.name,
      type: searchForm.type
    }
    const response = await axios.get('/category/page', { params })
    categoryList.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  loadCategoryList()
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
  loadCategoryList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadCategoryList()
}

// 添加分类
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑分类
const handleEdit = (row) => {
  categoryForm.id = row.id
  categoryForm.name = row.name
  categoryForm.type = row.type
  categoryForm.sort = row.sort
  categoryForm.status = row.status
  dialogVisible.value = true
}

// 删除分类
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/category/${id}`)
      ElMessage.success('删除成功')
      loadCategoryList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变更
const handleStatusChange = (row) => {
  // 这里可以调用API更新分类状态
  console.log('状态变更', row)
}

// 表单提交
const handleSubmit = async () => {
  if (!categoryFormRef.value) return
  
  await categoryFormRef.value.validate(async (valid) => {
    if (valid) {
      dialogLoading.value = true
      try {
        if (categoryForm.id) {
          // 编辑分类
          await axios.put(`/category/${categoryForm.id}`, categoryForm)
          ElMessage.success('编辑成功')
        } else {
          // 添加分类
          await axios.post('/category', categoryForm)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadCategoryList()
      } catch (error) {
        ElMessage.error(categoryForm.id ? '编辑失败' : '添加失败')
      } finally {
        dialogLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  categoryForm.id = ''
  categoryForm.name = ''
  categoryForm.type = 1
  categoryForm.sort = 0
  categoryForm.status = 1
  if (categoryFormRef.value) {
    categoryFormRef.value.resetFields()
  }
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 页面挂载时加载数据
onMounted(() => {
  loadCategoryList()
})
</script>

<style scoped>
.category-container {
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