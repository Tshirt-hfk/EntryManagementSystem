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
    { // 词条展示
      path: '/entry',
      name: 'entryPreview',
      component: () => import('../views/entry/entryPreview')
    },
    { // 词条编辑
      path: '/entryedit',
      name: 'entryEdit',
      component: () => import('../views/entry/entryEdit')
    },
    {  // 专题管理
      path: '/subjectmanagement',
      name: 'subjectManagement',
      component: () => import('../views/subjectMakerCenter/subjectManagement')
    },
    {
      path: '/subjectmakercenter',
      name: 'subjectMakerCenter',
      component: () => import('../views/subjectMakerCenter'),
      children: [
        {
          path: 'mysubject',
          name: 'mySubject',
          component: () => import('../views/subjectMakerCenter/mySubject')
        },
        {
          path: 'passwordmodify',
          name: 'passwordModify',
          component: () => import('../views/userCenter/passwordModify')
        },
        {
          path: 'emailmodify',
          name: 'emailModify',
          component: () => import('../views/userCenter/emailModify')
        },
        {
          path: 'myentry',
          name: 'myEntry',
          component: () => import('../views/userCenter/myEntry')
        }
      ]
    },
    {
      path: '/subject',
      name: 'subjectIndex',
      component: () => import('../views/subject/subjectIndex')
    },
    {
      path: '/subjectcreate',
      name: 'subjectCreate',
      component: () => import('../views/subject/subjectCreate')
    }
  ]
})