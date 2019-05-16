import loginService from '../../service/loginService';
import cookieService from '../../service/cookieService';

import util from '../../libs/util.js';

const user = {
    state: {
        userIdentity: cookieService.getUserIdentity(),
        token: cookieService.getToken(),
        roles: cookieService.getUserRoles(),
        friends: cookieService.getUserFriends()
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token;
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles;
        },
        SET_USER_IDENTITY: (state, userIdentity) => {
            state.userIdentity = userIdentity;
        },
        SET_FRIENDS: (state, friends) => {
            state.friends = friends;
        }
    },
    actions: {
        login ({ commit, dispatch }, params) {
            return loginService.login(params).then(
                resp => {
                    if (resp.data.code === 200) {
                        cookieService.setToken(resp.data.data);
                        commit('SET_TOKEN', resp.data.data);
                        // dispatch('getIdentityInfo');
                        return Promise.resolve(resp);
                    } else {
                        return Promise.reject();
                    }
                }
            ).catch(error => {
                return Promise.reject(error);
            });
        },
        logout ({commit}) {
            cookieService.clearCookies();
            commit('SET_TOKEN', '');
            commit('SET_USER_IDENTITY', {});
            commit('SET_ROLES', []);
            commit('SET_FRIENDS', []);
        },
        getIdentityInfo ({ commit }) {
            return loginService.getIdentityInfo().then(
                resp => {
                    if (resp.data.code === 200) {
                        const user = resp.data.data;
                        const userIdentity = util.simpleClone({}, user);
                        const userRoles = user.roles.map(function (item) {
                            return item.code;
                        });
                        const userFriends = user.friends.map(function (item) {
                            return item;
                        });
                        cookieService.setUserIdentity(userIdentity);
                        cookieService.setUserRoles(userRoles);
                        cookieService.setUserFriends(userFriends);
                        commit('SET_USER_IDENTITY', userIdentity);
                        commit('SET_ROLES', userRoles);
                        commit('SET_FRIENDS', userFriends);
                        commit('updateMenulist', userRoles);

                        return Promise.resolve(resp);
                    } else {
                        return Promise.reject();
                    }
                }
            ).catch(error => {
                return Promise.reject(error);
            });
        }
    },
    getters: {
        token: state => state.token,
        userIdentity: state => state.userIdentity,
        roles: state => state.roles,
        friends: state => state.friends,
    }
};

export default user;
