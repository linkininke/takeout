import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: 'system/user',
        name: 'UserList',
        component: () => import('../views/system/UserList.vue')
      },
      {
        path: 'system/role',
        name: 'RoleList',
        component: () => import('../views/system/RoleList.vue')
      },
      {
        path: 'system/permission',
        name: 'PermissionList',
        component: () => import('../views/system/PermissionList.vue')
      },
      {
        path: 'shop',
        name: 'Shop',
        component: () => import('../views/shop/Shop.vue')
      },
      {
        path: 'dish/category',
        name: 'DishCategory',
        component: () => import('../views/dish/Category.vue')
      },
      {
        path: 'dish/list',
        name: 'DishList',
        component: () => import('../views/dish/List.vue')
      },
      {
        path: 'setmeal',
        name: 'SetmealList',
        component: () => import('../views/setmeal/List.vue')
      },
  
      {
        path: 'order',
        name: 'OrderList',
        component: () => import('../views/order/List.vue')
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import('../views/Password.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router