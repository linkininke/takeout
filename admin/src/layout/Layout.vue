<template>
  <div class="layout-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="layout-aside">
        <div class="aside-header">
          <h3 v-if="!isCollapse" class="aside-title">苍穹外卖</h3>
          <el-icon v-else class="aside-icon"><Shop /></el-icon>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="layout-menu"
          background-color="#001529"
          text-color="#fff"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          router
        >
          <el-menu-item index="/">
            <template #icon>
              <el-icon><HomeFilled /></el-icon>
            </template>
            <template #title>首页</template>
          </el-menu-item>
          
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/user">用户管理</el-menu-item>
            <el-menu-item index="/system/role">角色管理</el-menu-item>
            <el-menu-item index="/system/permission">权限管理</el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/shop">
            <template #icon>
              <el-icon><Shop /></el-icon>
            </template>
            <template #title>店铺管理</template>
          </el-menu-item>
          
          <el-sub-menu index="dish">
            <template #title>
              <el-icon><Food /></el-icon>
              <span>菜品管理</span>
            </template>
            <el-menu-item index="/dish/category">菜品分类</el-menu-item>
            <el-menu-item index="/dish/list">菜品列表</el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/setmeal">
            <template #icon>
              <el-icon><Collection /></el-icon>
            </template>
            <template #title>套餐管理</template>
          </el-menu-item>
          
          <el-menu-item index="/order">
            <template #icon>
              <el-icon><Document /></el-icon>
            </template>
            <template #title>订单管理</template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header class="layout-header">
          <div class="header-left">
            <el-button
              type="text"
              @click="toggleCollapse"
              class="collapse-button"
            >
              <el-icon><Menu /></el-icon>
            </el-button>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-icon class="user-avatar"><User /></el-icon>
                <span>{{ userStore.userInfo?.name || '管理员' }}</span>
                <el-icon class="arrow-down"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">
                    <el-icon><SwitchButton /></el-icon>
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 主内容区域 -->
        <el-main class="layout-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/modules/user'
import {
  HomeFilled,
  Setting,
  Shop,
  Food,
  Collection,
  Document,
  Menu,
  User,
  ArrowDown,
  SwitchButton
} from '@element-plus/icons-vue'

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)

// 计算当前激活的菜单
const activeMenu = computed(() => {
  const path = route.path
  return path
})

// 切换侧边栏折叠状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  overflow: hidden;
}

.layout-aside {
  background-color: #001529;
  color: #fff;
  overflow: hidden;
  transition: width 0.3s;
}

.aside-header {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  border-bottom: 1px solid #1f2d3d;
}

.aside-title {
  margin: 0;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
}

.aside-icon {
  font-size: 24px;
  color: #fff;
}

.layout-menu {
  height: calc(100% - 60px);
  border-right: none;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-button {
  color: #606266;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  margin-right: 8px;
}

.arrow-down {
  margin-left: 8px;
  font-size: 12px;
}

.layout-main {
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
}
</style>