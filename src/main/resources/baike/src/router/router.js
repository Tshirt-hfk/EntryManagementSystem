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
    {
      path: '/usercenter',
      name: 'userCenter',
      component: () => import('../views/userCenter'),
      children: [
        {
          path: 'allmysubject',
          name: 'allMySubject',
          component: () => import('../views/userCenter/mySubject/allMySubject')
        },
        {  // 专题管理
          path: 'subjectmanagement',
          name: 'subjectManagement',
          component: () => import('../views/userCenter/mySubject/createdSubject/subjectManagement')
        },
        {
          path: 'entrymanagement',
          name: 'entryManagement',
          component: () => import('../views/userCenter/myEntry/entryManagement')
        },
        {
          path: 'identityverification',
          name: 'identityVerification',
          component: () => import('../views/userCenter/myInfo/identityVerification')
        },
        {
          path: 'passwordmodify',
          name: 'passwordModify',
          component: () => import('../views/userCenter/myInfo/passwordModify')
        },
        {
          path: 'emailmodify',
          name: 'emailModify',
          component: () => import('../views/userCenter/myInfo/emailModify')
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