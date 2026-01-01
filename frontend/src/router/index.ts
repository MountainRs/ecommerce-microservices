import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/pages/Home.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/Login.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/pages/Register.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/products',
    name: 'Products',
    component: () => import('@/pages/Products.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/products/:id',
    name: 'ProductDetail',
    component: () => import('@/pages/ProductDetail.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/pages/Profile.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/admin/products',
    name: 'AdminProducts',
    component: () => import('@/pages/admin/ProductManagement.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/pages/NotFound.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 从本地存储加载用户信息
  userStore.loadFromStorage()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    // 需要认证但未登录，重定向到登录页
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    // 已登录但访问登录/注册页，重定向到首页
    next('/')
  } else {
    next()
  }
})

export default router
