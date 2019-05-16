<style lang="less">
    @import './messageBox.less';
</style>

<template>
    <div style="position: relative;verflow: hidden;height: 99.5%">
        <div class="header">
            <span class="text">{{friendName}}</span>
        </div>
            <message class="messageStyle"></message>
        <Spin fix v-if="messageLoading">
            <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
            <div>正在加载聊天记录...</div>
        </Spin>
        <div class="textStyle">
            <textarea placeholder="按 Enter 发送" v-model="inputContent" @keyup="onKeyup"></textarea>
        </div>
    </div>
</template>

<script>
    /* eslint-disable quotes */

    import message from './components/message';
    import DateUtil from '../../libs/DateUtil';

    export default {
        components: {
            message
        },
        name: "messageBox",
        data() {
            return {
                inputContent: '',
                friendId: '',
                messageLoading: false,
                friendName: ''
            };
        },
        methods: {
            init () {
                this.friendId = parseInt(this.$route.params.userId);
                this.getFriendName();
                let params = {
                    sender: this.userId,
                    receiver: this.friendId,
                };
                this.messageLoading = true;
                this.$api.get('/chat/history/current', {params: params}).then((res) => {
                    if (res.data.code === 200) {
                        this.$store.state.messages = res.data.data;
                        this.friendList.map(item => {
                            if (item.id === this.friendId) {
                                item.count = 0;
                            }
                        });
                        this.friendList.push({});
                        this.friendList.pop();
                    } else {
                        this.$Message.error("获取聊天记录失败，错误代码：" + res.data.code);
                    }
                    this.messageLoading = false;
                });
            },
            onKeyup (e) {
                if (e.keyCode === 13 && this.inputContent.length) {
                    let date = new Date();
                    let message = {
                        sender: this.userId,
                        receiver: this.friendId,
                        content: this.inputContent,
                        time: DateUtil.formatDate(date, 'yyyy-MM-dd hh:mm:ss')
                    };
                    // 通过websocket发送消息
                    this.$stompClient.send('/chat', JSON.stringify(message), {});
                    // 存储历史记录
                    this.$api.post("/chat/history/add", message).then((res) => {
                    });
                    this.$store.state.messages.push({
                        content: this.inputContent,
                        date: date,
                        self: true
                    });
                    this.inputContent = '';
                }
            },
            getFriendName() {
                this.friendList.map(item => {
                    if (item.id === this.friendId) {
                        this.friendName = item.name;
                    }
                });
            },
        },
        watch: {
            // 如果路由有变化，会再次执行该方法
            '$route': 'init'
        },
        computed: {
            userId() {
                return this.$store.state.user.userIdentity.id;
            },
            friendList() {
                return this.$store.state.user.friends;
            },
        },
        created() {
            this.init();
        },
    };
</script>