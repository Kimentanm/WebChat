import util from '../libs/util';
import store from '../store';
import router from '../router';

const http = util.axios;

function handleError(error) {
    console.error(error);
    return Promise.reject(error);
}

// request拦截器
http.interceptors.request.use(config => {
    // Do something before request is sent
    if (store.getters.token) {
        config.headers['Authorization'] ="Bearer " +store.getters.token; // 让每个请求携带token--['X-Token']为自定义key 请根据实际情况自行修改
    }
    return config;
}, error => {
    handleError(error);
});

// respone拦截器
http.interceptors.response.use(response => {

        const code = response.data.code;
        if (code === 401) {
            // 登出
            store.dispatch('FedLogOut').then(() => {
                router.push({path: '/login'})
            });
        } else {
            return response
        }
    }
    ,
    error => {
        return Promise.reject(error);
    }
);


export default {
    //Common的CRUD
    get: function(url, params) {
        return http
            .get(url, params)
            .catch(handleError);
    },
    post: function(url, params) {
        return http
            .post(url, params)
            .catch(handleError);
    },
    put: function(url, params) {
        return http
            .put(url, params)
            .catch(handleError);
    },
    delete: function(url, params, success, failure) {
        return http
            .delete(url, params)
            .catch(handleError);
    },

    //其他其定义功能在这里定义
    download: function (url) {
        window.location.href = util.getBaseUrl() + url + '?token=Bearer ' + store.getters.token;
    }
}