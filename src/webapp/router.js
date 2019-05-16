import Vue from 'vue';
import iView from 'iview';
import Util from './libs/util';
import VueRouter from 'vue-router';
import {routers, otherRouter, appRouter} from './routers';
import Cookies from 'js-cookie';
import store from './store';
import cookieService from './service/cookieService';

Vue.use(VueRouter);

// 路由配置
const RouterConfig = {
    // mode: 'history',
    routes: routers
};

const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => {
    iView.LoadingBar.start();
    Util.title(to.meta.title);
    if (Cookies.get('locking') === '1' && to.name !== 'locking') {  // 判断当前是否是锁定状态
        next(false);
        router.replace({
            name: 'locking'
        });
    } else if (Cookies.get('locking') === '0' && to.name === 'locking') {
        next(false);
    } else {
        // if (!Cookies.get('user') && to.name !== 'login') {  // 判断是否已经登录且前往的页面不是登录页
        if (!cookieService.getToken() && to.name !== 'login') {  // 判断是否已经登录且前往的页面不是登录页
            next({
                name: 'login'
            });
            // } else if (Cookies.get('user') && to.name === 'login') {  // 判断是否已经登录且前往的是登录页
        } else if (cookieService.getToken() && to.name === 'login') {  // 判断是否已经登录且前往的是登录页
            Util.title();
            next({
                name: 'home_index'
            });
        } else {
            if (Util.getRouterObjByName([otherRouter, ...appRouter], to.name).roles !== undefined) {  // 判断用户是否有权限访问当前页
                if (store.state.user.roles.length === 0) {
                    store.dispatch('getIdentityInfo').then(resp => {
                        Util.toDefaultPage([otherRouter, ...appRouter], to.name, router, next);
                    }).catch(error => {
                        store.dispatch('logout').then(() => {
                            Vue.$Message.error('验证失败,请重新登录');
                            next({
                                name: 'login'
                            });
                        })
                    })
                } else {
                    if (Util.hasPermission(to.roles, store.state.user.roles)) {
                        Util.toDefaultPage([otherRouter, ...appRouter], to.name, router, next);
                    } else {
                        router.replace({
                            name: 'error_401'
                        });
                        next();
                    }
                }

            } else {
                Util.toDefaultPage([otherRouter, ...appRouter], to.name, router, next);
            }
        }
    }
    iView.LoadingBar.finish();
});

router.afterEach(() => {
    iView.LoadingBar.finish();
    window.scrollTo(0, 0);
});

export default router;