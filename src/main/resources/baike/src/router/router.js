import Vue from 'vue'
import Router from 'vue-router'
import index from '../views/index'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/register')
    },
    {
      path: '/entry',
      name: 'entryPreview',
      component: () => import('../views/entrypreview')
    },
    {
      path: '/subjectmaker',
      name: 'subjectMaker',
      component: () => import('../views/subjectmaker')
    },
    {
      path: '/subjectindex',
      name: 'subjectIndex',
      component: () => import('../views/subjectindex')
    },
    {
      path: '/subjectmanagement',
      name: 'subjectManagement',
      component: () => import('../views/subjectManagement')
    }
    // {
    //   path: '/information',
    //   meta:{
    //     requiresAuth: true
    //   },
    //   // redirect: {
    //   //   name: 'personMessage'
    //   // },
    //   name: 'information',
    //   component: () => import('../views/information'),
    //   children:[
    //     {
    //       path:'personMessage',
    //       name:'personMessage',
    //       component:() => import('../views/informations/personMessage'),
    //     },
    //     {
    //       path:'editMessage',
    //       component:() => import('../views/informations/editMessage'),
    //     },
    //     {
    //       path:'editPassword',
    //       component:() => import('../views/informations/editPassword'),
    //     },
    //     {
    //       path:'mybstopic',
    //       component:() => import('../views/informations/mybstopic'),
    //     }
    //   ]
    // },
    // {
    //   path: '/administrator',
    //   meta:{
    //     requiresAuth: true
    //   },
    //   // redirect: {
    //   //   name: 'personMessage'
    //   // },
    //   name: 'administrator',
    //   component: () => import('../views/administrator'),
    //   children:[
    //     {
    //       path:'blogManagement',
    //       name:'blogManagement',
    //       component:() => import('../views/administration/blogManagement'),
    //     },
    //     {
    //       path:'userManagement',
    //       name:'userManagement',
    //       component:() => import('../views/administration/userManagement'),
    //     },{
    //       path:'blogContentManagement',
    //       name:'blogContentManagement',
    //       component:() => import('../views/administration/blogContentManagement')
    //     }
    //   ]
    // }
  ]
})