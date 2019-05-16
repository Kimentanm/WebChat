import Vue from 'vue';
import Vuex from 'vuex';
import state from './state'
import getters from './getters'
import mutations from './mutations'
import actions from './actions'
import user from './modules/user'

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        user
    },
    state: state,
    getters: getters,
    mutations: mutations,
    actions: actions
});


export default store;
