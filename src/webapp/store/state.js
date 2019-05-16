
import {routers, otherRouter, appRouter} from '../routers';

export default {
    routers: [
        otherRouter,
        ...appRouter
    ],
    menuList: [],
    tagsList: [...otherRouter.children],
    pageOpenedList: [{
        title: '首页',
        path: '',
        name: 'home_index'
    }],
    currentPageName: '',
    currentPath: [
        {
            title: '首页',
            path: '',
            name: 'home_index'
        }
    ], // 面包屑数组
    openedSubmenuArr: [], // 要展开的菜单数组
    menuTheme: '', // 主题
    theme: '',
    cachePage: [],
    lang: '',
    isFullScreen: false,
    dontCache: ['text-editor'], // 在这里定义你不想要缓存的页面的name属性值(参见路由配置router.js)
    messages: [],
    currentFriendId: '',
}