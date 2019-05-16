import Vue from 'vue';
import iView from 'iview';
import {appRouter} from './routers';
import App from './app.vue';
import api from './service/api';
import 'iview/dist/styles/iview.css';

import router from './router';
import store from './store';
import VueStomp from 'vue-stomp/src';

Vue.use(VueStomp);
Vue.use(iView);

// 自动设置语言
const navLang = navigator.language;
const localLang = (navLang === 'zh-CN' || navLang === 'en-US') ? navLang : false;
const lang = window.localStorage.lang || localLang || 'zh-CN';

Vue.config.lang = lang;

Vue.prototype.$api = api;

new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App),
    data: {
        currentPageName: ''
    },
    mounted () {
        this.currentPageName = this.$route.name;
        this.$store.commit('initCachepage');
        // // 权限菜单过滤相关
        this.$store.commit('updateMenulist', this.$store.state.user.roles);
    },
    created () {
        let tagsList = [];
        appRouter.map((item) => {
            if (item.children.length <= 1) {
                tagsList.push(item.children[0]);
            } else {
                tagsList.push(...item.children);
            }
        });
        this.$store.commit('setTagsList', tagsList);
    }
});
