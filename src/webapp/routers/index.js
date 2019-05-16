import Main from '../views/Main.vue';

// 不作为Main组件的子页面展示的页面单独写，如下
export const loginRouter = {
    path: '/login',
    name: 'login',
    meta: {
        title: '登录Web聊天室'
    },
    component: resolve => {require(['../views/login.vue'], resolve);}
};

// export const homeRouter = {
//     path: '/',
//     icon: 'home',
//     name: 'home_router',
//     title: '首页',
//     component: Main,
//     access: 0,
//     children: [
//         { path: 'home', title: '首页', name: 'home_index', component: resolve => { require(['../views/home/home.vue'], resolve); } },
//     ]
// };

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
    path: '/',
    name: 'otherRouter',
    redirect: '/home',
    component: Main,
    children: [
        { path: 'ownspace', title: '个人中心', name: 'ownspace_index', component: () => import('../views/setting/own-space/own-space.vue') },
        { path: 'messageBox/:userId', title: '聊天对话框', name: 'messageBox', component: () => import('../views/chat/messageBox.vue') },
        { path: 'home', title: '首页', name: 'home_index', component: resolve => { require(['../views/home/home.vue'], resolve); } },
    ]
};

// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
];

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
    loginRouter,
    // homeRouter,
    otherRouter,
    ...appRouter,
];
