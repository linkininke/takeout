<template>
  <div class="order-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>订单管理</h2>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form
        :model="searchForm"
        label-position="right"
        label-width="120px"
        class="search-form"
      >
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="订单号">
              <el-input v-model="searchForm.orderNumber" placeholder="请输入订单号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="顾客名称">
              <el-input v-model="searchForm.customerName" placeholder="请输入顾客名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="订单状态">
              <el-select v-model="searchForm.status" placeholder="请选择订单状态">
                <el-option label="待付款" :value="1"></el-option>
                <el-option label="待接单" :value="2"></el-option>
                <el-option label="待配送" :value="3"></el-option>
                <el-option label="已完成" :value="4"></el-option>
                <el-option label="已取消" :value="5"></el-option>
                <el-option label="退款中" :value="6"></el-option>
                <el-option label="已退款" :value="7"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="支付状态">
              <el-select v-model="searchForm.payStatus" placeholder="请选择支付状态">
                <el-option label="未支付" :value="0"></el-option>
                <el-option label="已支付" :value="1"></el-option>
                <el-option label="已退款" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="search-buttons">
          <el-col :span="24" style="text-align: right;">
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><RefreshRight /></el-icon>
              重置
            </el-button>
          </el-col>
        </el-row>
      </el-form>
      
      <!-- 订单列表 -->
      <el-table
        v-loading="loading"
        :data="orderList"
        style="width: 100%"
        border
      >
        <el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="150"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="scope">
            {{ scope.row.totalAmount }} 元
          </template>
        </el-table-column>
        <el-table-column prop="paymentAmount" label="实付金额" width="120">
          <template #default="scope">
            {{ scope.row.paymentAmount }} 元
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payStatus" label="支付状态" width="120">
          <template #default="scope">
            <el-tag :type="getPayStatusTagType(scope.row.payStatus)">
              {{ getPayStatusText(scope.row.payStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewDetail(scope.row)">
              <el-icon><View /></el-icon>
              详情
            </el-button>
            <el-button type="success" size="small" @click="handleUpdateStatus(scope.row)" v-if="scope.row.status < 4">
              <el-icon><Check /></el-icon>
              更新状态
            </el-button>
            <el-button type="danger" size="small" @click="handleCancelOrder(scope.row)" v-if="scope.row.status < 3">
              <el-icon><Delete /></el-icon>
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
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
      
      <!-- 订单详情对话框 -->
      <el-dialog
        v-model="detailDialogVisible"
        title="订单详情"
        width="80%"
      >
        <div v-if="currentOrder" class="order-detail">
          <h3>订单基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ currentOrder.orderNumber }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ currentOrder.createTime }}</el-descriptions-item>
            <el-descriptions-item label="收货人">{{ currentOrder.consignee }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentOrder.phone }}</el-descriptions-item>
            <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.address }}</el-descriptions-item>
            <el-descriptions-item label="总金额">{{ currentOrder.totalAmount }} 元</el-descriptions-item>
            <el-descriptions-item label="优惠金额">{{ currentOrder.discountAmount }} 元</el-descriptions-item>
            <el-descriptions-item label="实付金额">{{ currentOrder.paymentAmount }} 元</el-descriptions-item>
            <el-descriptions-item label="订单状态">{{ getStatusText(currentOrder.status) }}</el-descriptions-item>
            <el-descriptions-item label="支付状态">{{ getPayStatusText(currentOrder.payStatus) }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ getPayMethodText(currentOrder.payMethod) }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ currentOrder.remark || '-' }}</el-descriptions-item>
          </el-descriptions>
          
          <h3 style="margin-top: 20px;">订单明细</h3>
          <el-table :data="orderDetails" border style="margin-top: 10px;">
            <el-table-column prop="name" label="商品名称" width="200"></el-table-column>
            <el-table-column prop="amount" label="单价" width="100">
              <template #default="scope">
                {{ scope.row.amount }} 元
              </template>
            </el-table-column>
            <el-table-column prop="copies" label="数量" width="100"></el-table-column>
            <el-table-column label="小计" width="120">
              <template #default="scope">
                {{ (scope.row.amount * scope.row.copies).toFixed(2) }} 元
              </template>
            </el-table-column>
            <el-table-column prop="dishFlavor" label="口味" width="150"></el-table-column>
          </el-table>
        </div>
      </el-dialog>
      
      <!-- 更新状态对话框 -->
      <el-dialog
        v-model="statusDialogVisible"
        title="更新订单状态"
        width="400px"
      >
        <el-form :model="statusForm" label-position="right" label-width="100px">
          <el-form-item label="当前状态">
            <el-tag :type="getStatusTagType(currentOrder?.status)">
              {{ getStatusText(currentOrder?.status) }}
            </el-tag>
          </el-form-item>
          <el-form-item label="目标状态">
            <el-select v-model="statusForm.status" placeholder="请选择目标状态">
              <el-option label="待付款" :value="1" v-if="currentOrder?.status < 1"></el-option>
              <el-option label="待接单" :value="2" v-if="currentOrder?.status < 2"></el-option>
              <el-option label="待配送" :value="3" v-if="currentOrder?.status < 3"></el-option>
              <el-option label="已完成" :value="4" v-if="currentOrder?.status < 4"></el-option>
              <el-option label="已取消" :value="5" v-if="currentOrder?.status < 5"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="statusDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleConfirmStatusUpdate">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, View, Check, Delete } from '@element-plus/icons-vue'
import axios from '../../utils/axios'

// 状态定义
const loading = ref(false)
const orderList = ref([])
const detailDialogVisible = ref(false)
const statusDialogVisible = ref(false)
const currentOrder = ref(null)
const orderDetails = ref([])

// 搜索表单
const searchForm = reactive({
  orderNumber: '',
  customerName: '',
  status: '',
  payStatus: ''
})

// 分页信息
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 状态更新表单
const statusForm = reactive({
  status: ''
})

// 加载订单列表
const loadOrderList = async () => {
  loading.value = true
  try {
    const response = await axios.get('/orders/page', {
      params: {
        page: pagination.currentPage,
        pageSize: pagination.pageSize
      }
    })
    orderList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索订单
const handleSearch = () => {
  pagination.currentPage = 1
  loadOrderList()
}

// 重置搜索表单
const handleReset = () => {
  Object.assign(searchForm, {
    orderNumber: '',
    customerName: '',
    status: '',
    payStatus: ''
  })
  pagination.currentPage = 1
  loadOrderList()
}

// 查看订单详情
const handleViewDetail = async (order) => {
  currentOrder.value = order
  detailDialogVisible.value = true
  try {
    const response = await axios.get(`/order-detail/order/${order.id}`)
    orderDetails.value = response.data
  } catch (error) {
    ElMessage.error('获取订单明细失败')
  }
}

// 更新订单状态
const handleUpdateStatus = (order) => {
  currentOrder.value = order
  statusForm.status = order.status + 1
  statusDialogVisible.value = true
}

// 确认更新状态
const handleConfirmStatusUpdate = async () => {
  if (!statusForm.status) {
    ElMessage.warning('请选择目标状态')
    return
  }
  
  try {
    await axios.put(`/orders/${currentOrder.value.id}/status?status=${statusForm.status}`)
    ElMessage.success('订单状态更新成功')
    statusDialogVisible.value = false
    loadOrderList()
  } catch (error) {
    ElMessage.error('更新订单状态失败')
  }
}

// 取消订单
const handleCancelOrder = async (order) => {
  await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await axios.put(`/orders/${order.id}/status?status=5`)
      ElMessage.success('订单已取消')
      loadOrderList()
    } catch (error) {
      ElMessage.error('取消订单失败')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  loadOrderList()
}

// 当前页码变化
const handleCurrentChange = (current) => {
  pagination.currentPage = current
  loadOrderList()
}

// 获取订单状态文本
const getStatusText = (status) => {
  const statusMap = {
    1: '待付款',
    2: '待接单',
    3: '待配送',
    4: '已完成',
    5: '已取消',
    6: '退款中',
    7: '已退款'
  }
  return statusMap[status] || '未知状态'
}

// 获取订单状态标签类型
const getStatusTagType = (status) => {
  const typeMap = {
    1: 'warning',
    2: 'info',
    3: 'primary',
    4: 'success',
    5: 'danger',
    6: 'warning',
    7: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取支付状态文本
const getPayStatusText = (payStatus) => {
  const statusMap = {
    0: '未支付',
    1: '已支付',
    2: '已退款'
  }
  return statusMap[payStatus] || '未知状态'
}

// 获取支付状态标签类型
const getPayStatusTagType = (payStatus) => {
  const typeMap = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  }
  return typeMap[payStatus] || 'info'
}

// 获取支付方式文本
const getPayMethodText = (payMethod) => {
  const methodMap = {
    1: '微信支付'
  }
  return methodMap[payMethod] || '未知方式'
}

// 页面挂载时加载数据
onMounted(() => {
  loadOrderList()
})
</script>

<style scoped>
.order-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}

.search-buttons {
  margin-top: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.order-detail h3 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}
</style>