import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/',redirect:'/manager/home'},
    
    // 教练后台路由
    {path: '/coachManager', component: () => import('../views/CoachManager.vue'),
      children: [
        {path: 'home', meta: {name: '首页'}, component: () => import('../views/CoachHome.vue')},
        {path: 'notice', meta: {name: '健身公告'}, component: () => import('../views/Notice.vue')},
        {path: 'course', meta: {name: '我的课程'}, component: () => import('../views/CoachCourse.vue')},
        {path: 'info', meta: {name: '个人信息'}, component: () => import('../views/Info.vue')},
        {path: 'updatePassword', meta: {name: '修改密码'}, component: () => import('../views/UpdatePassword.vue')},
      ]
    },
    
    {path: '/manager',  component: () => import('../views/Manager.vue'),
        children:[
        {path: 'home', meta:{name:'主页'} , component: () =>  import('../views/Home.vue'),},
        {path: 'admin', meta:{name:'管理员信息'} ,  component: () =>  import('../views/Admin.vue'),},
        {path: 'user', meta:{name:'会员信息'} ,  component: () =>  import('../views/User.vue'),},
        {path: 'info', meta:{name:'个人信息'} ,  component: () =>  import('../views/Info.vue'),},
        {path: 'updatePassword', meta:{name:'修改密码'} ,  component: () =>  import('../views/UpdatePassword.vue'),},
        {path: 'notice', meta:{name:'健身公告'} ,  component: () =>  import('../views/Notice.vue'),},
        {path: 'category', meta:{name:'健身分类'} ,  component: () =>  import('../views/Category.vue'),},
        {path: 'introduction', meta:{name:'健身攻略'} ,  component: () =>  import('../views/Introduction.vue'),},
        {path: 'apply', meta:{name:'会员课程'} ,  component: () =>  import('../views/Apply.vue'),},
        {path: 'coach', meta:{name:'教练信息'} ,  component: () =>  import('../views/Coach.vue'),},
        {path: 'course', meta:{name:'课程信息管理'} ,  component: () =>  import('../views/Course.vue'),},
        {path: 'record', meta:{name:'教练预约信息'} ,  component: () =>  import('../views/Record.vue'),},
        {path: 'mailSchedule', meta:{name:'邮件定时任务'} ,  component: () =>  import('../views/MailSchedule.vue'),},
        {path: 'logs', meta:{name:'会员日志'} ,  component: () =>  import('../views/Logs.vue'),},
        {path: 'balance', meta:{name:'我的余额'} ,  component: () =>  import('../views/Balance.vue'),},
      ]},

      {path: '/front',  component: () => import('../views/Front.vue'),
          children:[
              {path: 'home', meta:{name:'系统主页'} , component: () =>  import('../views/Home.vue'),},
          ]},
      {path: '/front/course', meta:{name:'精品课程'}, component: () => import('../views/FrontCourse.vue')},

    // {path: '/front/home',  component: import('../views/Front.vue'),},
    {path: '/front/introductionDetail',  component: import('../views/IntroductionDetail.vue'),},
    {path: '/front/home/noticeDetail',  component: import('../views/NoticeDetail.vue'),},
    {path: '/front/home/frontnotice',  component: import('../views/FrontNotice.vue'),},
      { path: '/login', component: () => import('@/views/Login.vue') },
    {path: '/register',  component: () =>import('../views/Register.vue'),},
    {path: '/notFound',  component: () =>import('../views/404.vue'),},
    {path: '/:pathMatch(.*)', redirect:'/notFound'},
  ],
})

export default router
