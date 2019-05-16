<style lang="less">
    @import "./home.less";
    @import "../../styles/common.less";
</style>
<style>
    .home-company-welcome {
        background-image: url('../../styles/home_welcome_background.jpg');
        width: 100%;
        height: 100%;
        background-size: cover;
        background-position: center;
        position: relative;
        text-align: center;
        padding-top: 40%;
    }

    .welcome-text {
        color: #FFFFFF;
        font-size: 30px;
    }
</style>
<template>
    <div style="width: 100%;height: 100%">
        <div class="home-company-welcome">
            <span class="welcome-text">[ 欢迎使用Web聊天室1.0 ]</span>
        </div>
    </div>
</template>

<script>
    import env from '../../config/env';
    import Notice from '../main_components/notice';

    export default {
        components: {
            Notice
        },
        name: 'home',
        data () {
            return {
                // 如果不需要websocket支持，请关闭此选项
                enableStomp: true,
                // 禁用默认的自动连接，使用onConnectionInactive代替
                stompReconnect: false
            };
        },
        computed: {
            userId () {
                return this.$store.state.user.userIdentity.id;
            },
            userInfo () {
                return this.$store.state.user.userIdentity;
            },
            currentFriendId () {
                return this.$store.state.currentFriendId;
            },
            friendList () {
                return this.$store.state.user.friends;
            }
        },
        methods: {
            onConnected (frame) {
                console.log('Connected: ' + frame);
                this.$stompClient.subscribe('/topic/' + this.userId, this.onMessage, this.onFailed);
            },
            onMessage (data) {
                let body = JSON.parse(data.body);
                switch (body.flag) {
                    case 'chat' :
                        if (body.sender === this.currentFriendId) {
                            this.$store.state.messages.push(body);
                            this.$api.get('/chat/history/read/' + this.currentFriendId);
                        } else {
                            let friend;
                            this.friendList.map(item => {
                                if (item.id === body.sender) {
                                    item.count++;
                                    friend = item;
                                }
                            });
                            Notice.info({
                                title: friend.name + '发来一条消息：',
                                desc: body.content,
                                imgSrc: friend.imageUrl,
                                duration: 4
                            });
                            this.friendList.push({});
                            this.friendList.pop();
                        }
                        break;
                    case 'addFriend' : {
                        console.log('添加好友');
                        this.$store.state.user.friends.push(body.user);
                        break;
                    }
                    case 'sendMessage' : {
                        this.userInfo.messageUnread = true;
                    }
                    default:break;
                }
            },
            onFailed (frame) {
                console.log('连接失败：' + frame);
            },
            connectSrv () {
                let url;
                if (env === 'development') {
                    url = 'http://localhost:8082/hsd-server/endpointChat';
                } else {
                    url = 'http://111.231.135.83/hsd-server/endpointChat';
                }
                this.connetWM(url, {}, this.onConnected, this.onConnectionInactive);
            },
            onConnectionInactive (errorEvent) {
                if (errorEvent.type === 'close') {
                    if (this.$store.state.user.userIdentity.id) {
                        setTimeout(this.connectSrv, 5000);
                    }
                }
            },
            disconnect () {
                this.disconnetWM();
            }
        },
        mounted () {
            if (this.enableStomp) {
                this.connectSrv();
            }
        }
    };
</script>
