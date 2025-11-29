<template>
  <div class="home-container">
    <el-card class="home-card">
      <template #header>
        <div class="card-header">
          <h2>欢迎使用苍穹外卖管理系统</h2>
        </div>
      </template>
      <div class="home-content">
        <!-- 统计卡片 -->
        <div class="stats-grid">
          <el-statistic
            v-for="(stat, index) in stats"
            :key="index"
            class="stat-item"
            :title="stat.title"
            :value="stat.value"
            :precision="stat.precision || 0"
          >
            <template #prefix>
              <el-icon :size="24"><component :is="stat.icon" /></el-icon>
            </template>
          </el-statistic>
        </div>
        
        <!-- 图表区域 -->
        <div class="charts-container">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <h3>销售额趋势</h3>
              </div>
            </template>
            <div ref="salesChartRef" class="chart"></div>
          </el-card>
          
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <h3>订单来源分析</h3>
              </div>
            </template>
            <div ref="orderSourceChartRef" class="chart"></div>
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  Shop,
  Food,
  Document,
  User,
  ArrowUp
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const stats = ref([
  {
    title: '今日订单',
    value: 128,
    icon: Document,
    color: '#3f8600'
  },
  {
    title: '今日销售额',
    value: 2888.88,
    precision: 2,
    icon: ArrowUp,
    color: '#13ce66'
  },
  {
    title: '菜品总数',
    value: 256,
    icon: Food,
    color: '#1890ff'
  },
  {
    title: '店铺数量',
    value: 1,
    icon: Shop,
    color: '#722ed1'
  }
])

// 图表引用
const salesChartRef = ref(null)
const orderSourceChartRef = ref(null)

// 图表实例
let salesChart = null
let orderSourceChart = null

// 初始化销售额趋势图
const initSalesChart = () => {
  if (salesChartRef.value) {
    salesChart = echarts.init(salesChartRef.value)
    const option = {
      tooltip: {
        trigger: 'axis',
        formatter: '{b}: {c}元'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          formatter: '{value}元'
        }
      },
      series: [
        {
          name: '销售额',
          type: 'line',
          data: [1200, 1900, 3000, 2300, 2900, 4500, 3200],
          smooth: true,
          itemStyle: {
            color: '#1890ff'
          },
          areaStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0, color: 'rgba(24, 144, 255, 0.3)'
              }, {
                offset: 1, color: 'rgba(24, 144, 255, 0.05)'
              }]
            }
          }
        }
      ]
    }
    salesChart.setOption(option)
  }
}

// 初始化订单来源分析图
const initOrderSourceChart = () => {
  if (orderSourceChartRef.value) {
    orderSourceChart = echarts.init(orderSourceChartRef.value)
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ['堂食', '外卖', '自取']
      },
      series: [
        {
          name: '订单来源',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 35, name: '堂食' },
            { value: 55, name: '外卖' },
            { value: 10, name: '自取' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    orderSourceChart.setOption(option)
  }
}

// 监听窗口大小变化，自适应调整图表
const handleResize = () => {
  salesChart?.resize()
  orderSourceChart?.resize()
}

onMounted(() => {
  initSalesChart()
  initOrderSourceChart()
  window.addEventListener('resize', handleResize)
})
</script>

<style scoped>
.home-container {
  height: 100%;
}

.home-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.home-content {
  padding: 20px 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-item {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.charts-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-card {
  height: 400px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart {
  width: 100%;
  height: calc(100% - 50px);
}

@media (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}
</style>