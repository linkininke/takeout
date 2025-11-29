<template>
  <div class="setmeal-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>套餐管理</h2>
          <el-button type="primary" @click="handleAdd" class="add-button">
            <el-icon><Plus /></el-icon>
            添加套餐
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" class="search-form" @submit.prevent>
        <el-form-item label="套餐名称">
          <el-input v-model="searchForm.name" placeholder="请输入套餐名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="套餐分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择套餐分类" clearable>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="套餐状态">
          <el-select v-model="searchForm.status" placeholder="请选择套餐状态" clearable>
            <el-option label="起售" value="1"></el-option>
            <el-option label="停售" value="0"></el-option>
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

      <!-- 套餐列表 -->
      <el-table
        v-loading="loading"
        :data="setmealList"
        style="width: 100%"
        border
        stripe
      >
        <el-table-column type="index" label="序号" width="80"></el-table-column>
        <el-table-column prop="name" label="套餐名称" width="180"></el-table-column>
        <el-table-column prop="categoryId" label="套餐分类" width="120">
          <template #default="scope">
            {{ getCategoryName(scope.row.categoryId) }}
          </template>
        </el-table-column>
        <el-table-column prop="price" label="套餐价格" width="120">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="image" label="套餐图片" width="120">
          <template #default="scope">
            <el-image
              :src="scope.row.image"
              :preview-src-list="[scope.row.image]"
              fit="cover"
              style="width: 60px; height: 60px; border-radius: 4px; cursor: pointer;"
            ></el-image>
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="setmealFormRef"
        :model="setmealForm"
        :rules="setmealRules"
        label-position="right"
        label-width="120px"
      >
        <el-form-item label="套餐名称" prop="name">
          <el-input v-model="setmealForm.name" placeholder="请输入套餐名称"></el-input>
        </el-form-item>
        <el-form-item label="套餐分类" prop="categoryId">
          <el-select v-model="setmealForm.categoryId" placeholder="请选择套餐分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="套餐价格" prop="price">
          <el-input-number
            v-model="setmealForm.price"
            :min="0"
            :step="0.1"
            :precision="2"
            placeholder="请输入套餐价格"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="套餐图片" prop="image">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :on-success="handleImageUploadSuccess"
            :before-upload="beforeImageUpload"
            :http-request="customImageUpload"
          >
            <img v-if="setmealForm.image" :src="setmealForm.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="套餐描述" prop="description">
          <el-input
            v-model="setmealForm.description"
            type="textarea"
            placeholder="请输入套餐描述"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="setmealForm.sort" :min="0" :step="1" placeholder="请输入排序"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="setmealForm.status"
            active-value="1"
            inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="包含菜品" prop="setmealDishes">
          <el-button type="primary" size="small" @click="handleAddDish">
            <el-icon><Plus /></el-icon>
            添加菜品
          </el-button>
          <el-table
            :data="setmealForm.setmealDishes"
            style="width: 100%; margin-top: 10px"
            border
          >
            <el-table-column prop="dishId" label="菜品ID" width="100"></el-table-column>
            <el-table-column prop="name" label="菜品名称" width="150"></el-table-column>
            <el-table-column prop="price" label="菜品价格" width="100">
              <template #default="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="copies" label="菜品份数" width="100">
              <template #default="scope">
                <el-input-number
                  v-model="scope.row.copies"
                  :min="1"
                  :step="1"
                  style="width: 100px"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button type="danger" size="small" @click="handleRemoveDish(scope.$index)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
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
const setmealFormRef = ref(null)

// 搜索表单
const searchForm = reactive({
  name: '',
  categoryId: '',
  status: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 套餐列表
const setmealList = ref([])

// 分类列表
const categories = ref([])

// 菜品列表
const dishes = ref([])

// 套餐表单
const setmealForm = reactive({
  id: '',
  name: '',
  categoryId: '',
  price: 0,
  image: '',
  description: '',
  sort: 0,
  status: 1,
  setmealDishes: []
})

// 表单验证规则
const setmealRules = {
  name: [
    { required: true, message: '请输入套餐名称', trigger: 'blur' },
    { min: 2, max: 50, message: '套餐名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择套餐分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入套餐价格', trigger: 'blur' }
  ],
  setmealDishes: [
    { required: true, message: '请至少添加一个菜品', trigger: 'change' },
    { type: 'array', min: 1, message: '请至少添加一个菜品', trigger: 'change' }
  ]
}

// 对话框标题
const dialogTitle = computed(() => {
  return setmealForm.id ? '编辑套餐' : '添加套餐'
})

// 获取分类名称
const getCategoryName = (categoryId) => {
  const category = categories.value.find(c => c.id === categoryId)
  return category ? category.name : ''
}

// 加载套餐列表
const loadSetmealList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      name: searchForm.name,
      categoryId: searchForm.categoryId,
      status: searchForm.status
    }
    const response = await axios.get('/setmeal/page', { params })
    setmealList.value = response.data.records || []
    pagination.total = response.data.total || 0
  } catch (error) {
    ElMessage.error('获取套餐列表失败')
  } finally {
    loading.value = false
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    // 只加载套餐分类（type=2）
    const response = await axios.get(`/category/list?type=2`)
    categories.value = response.data || []
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

// 加载菜品列表
const loadDishes = async () => {
  try {
    const response = await axios.get('/dish/list')
    dishes.value = response.data || []
  } catch (error) {
    ElMessage.error('获取菜品列表失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1
  loadSetmealList()
}

// 重置
const handleReset = () => {
  searchForm.name = ''
  searchForm.categoryId = ''
  searchForm.status = ''
  handleSearch()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadSetmealList()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadSetmealList()
}

// 添加套餐
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑套餐
const handleEdit = (row) => {
  setmealForm.id = row.id
  setmealForm.name = row.name
  setmealForm.categoryId = row.categoryId
  setmealForm.price = row.price
  setmealForm.image = row.image
  setmealForm.description = row.description
  setmealForm.sort = row.sort
  setmealForm.status = row.status
  
  // 加载套餐菜品
  loadSetmealDishes(row.id)
  dialogVisible.value = true
}

// 加载套餐菜品
const loadSetmealDishes = async (setmealId) => {
  try {
    const response = await axios.get(`/setmeal/${setmealId}`)
    setmealForm.setmealDishes = response.data.setmealDishes || []
  } catch (error) {
    ElMessage.error('获取套餐菜品失败')
  }
}

// 删除套餐
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该套餐吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete(`/setmeal/${id}`)
      ElMessage.success('删除成功')
      loadSetmealList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 状态变更
const handleStatusChange = (row) => {
  // 这里可以调用API更新套餐状态
  console.log('状态变更', row)
}

// 表单提交
const handleSubmit = async () => {
  if (!setmealFormRef.value) return
  
  await setmealFormRef.value.validate(async (valid) => {
    if (valid) {
      dialogLoading.value = true
      try {
        if (setmealForm.id) {
          // 编辑套餐
          await axios.put(`/setmeal/${setmealForm.id}`, setmealForm)
          ElMessage.success('编辑成功')
        } else {
          // 添加套餐
          await axios.post('/setmeal', setmealForm)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadSetmealList()
      } catch (error) {
        ElMessage.error(setmealForm.id ? '编辑失败' : '添加失败')
      } finally {
        dialogLoading.value = false
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  setmealForm.id = ''
  setmealForm.name = ''
  setmealForm.categoryId = ''
  setmealForm.price = 0
  setmealForm.image = ''
  setmealForm.description = ''
  setmealForm.sort = 0
  setmealForm.status = 1
  setmealForm.setmealDishes = []
  if (setmealFormRef.value) {
    setmealFormRef.value.resetFields()
  }
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 添加菜品
const handleAddDish = () => {
  // 这里可以实现选择菜品的逻辑
  // 暂时添加一个默认菜品
  setmealForm.setmealDishes.push({
    dishId: '',
    name: '',
    price: 0,
    copies: 1
  })
}

// 移除菜品
const handleRemoveDish = (index) => {
  setmealForm.setmealDishes.splice(index, 1)
}

// 图片上传前验证
const beforeImageUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// 图片上传成功处理
const handleImageUploadSuccess = (response) => {
  // 这里处理上传成功后的逻辑
  console.log('图片上传成功', response)
}

// 自定义图片上传
const customImageUpload = (options) => {
  // 这里实现自定义上传逻辑，调用API上传图片
  console.log('自定义图片上传', options)
  // 模拟上传成功
  setTimeout(() => {
    options.onSuccess({ url: 'https://picsum.photos/200/200' })
    setmealForm.image = 'https://picsum.photos/200/200'
  }, 1000)
}

// 页面挂载时加载数据
onMounted(() => {
  loadCategories()
  loadSetmealList()
  loadDishes()
})
</script>

<style scoped>
.setmeal-list-container {
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
</style>