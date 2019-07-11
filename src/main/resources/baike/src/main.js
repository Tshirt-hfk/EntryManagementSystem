// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App.vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router/router.js'
import axios from 'axios'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

Vue.use(Vuex)
Vue.use(ElementUI)
Vue.use(mavonEditor)
Vue.config.productionTip = false

// 全局注册，使用方法为:this.$axios
Object.defineProperty(Vue.prototype, '$axios', { value: axios })

axios.interceptors.request.use(function (config) {
  let token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
}, function (error) {
  return Promise.reject(error)
})

const store = new Vuex.Store({
  state: {
    status: '0'    // 用户身份，0:未登录；1:普通用户；2:专题制作人；3:系统管理员
  },
  getters: {
    status: state => state.status
  },
  mutations: {
    status (state, status) {
      state.status = status
    }
  }
})

router.beforeEach((to, from, next) => {
  let token = localStorage.getItem('token')
  if (to.path === '/' || to.path === '/entry') {
    return next()
  }
  if (!token && to.path !== '/login' && to.path !== '/register') {
    return next('/login')
  }
  next()
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
