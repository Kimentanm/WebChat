import api from './api';
import urls from './urls';

export default {
    login: function (payload) {
        return api.post(urls.authenticate_url, payload);
    },
    getIdentityInfo: function () {
        return api.get(urls.identityInfo_url);
    }

};