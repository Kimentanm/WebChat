
import Cookies from 'js-cookie';

const TokenKey = 'HSD-JWT-Auth-Token';
const UserKey = 'HSD-User-Key';
const UserRoleKey = 'HSD-User-Role-Key';
const UserFriendKey = 'HSD-User-Friend-Key';

export default {

    getToken: function () {
        return Cookies.get(TokenKey);
    },

    setToken: function (token) {
        Cookies.set(TokenKey, token);
    },

    removeToken: function () {
        Cookies.remove(TokenKey);
    },

    setUserIdentity: function (user) {
        Cookies.set(UserKey, user);
    },

    getUserIdentity: function () {
        let user = Cookies.get(UserKey);
        try {
            return JSON.parse(user);
        } catch (err) {
            return {};
        }
    },

    removeUserIdentity: function () {
        Cookies.remove(UserKey);
    },

    setUserRoles: function (userRoles) {
        Cookies.set(UserRoleKey, userRoles);
    },

    getUserRoles: function () {
        let roles = Cookies.get(UserRoleKey);
        try {
            return JSON.parse(roles);
        } catch (err) {
            return [];
        }
    },

    setUserFriends: function (userFriends) {
        Cookies.set(UserFriendKey, userFriends);
    },

    getUserFriends: function () {
        let friends = Cookies.get(UserFriendKey);
        try {
            return JSON.parse(friends);
        } catch (err) {
            return [];
        }
    },

    removeUserRoles: function () {
        Cookies.remove(UserRoleKey);
    },

    clearCookies: function () {
        this.removeToken();
        this.removeUserIdentity();
        this.removeUserRoles();
    }
};
