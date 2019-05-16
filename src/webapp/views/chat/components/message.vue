<style lang="less" scoped>
    @import './message.less';
</style>

<template>
    <div>
        <div class="message" v-scroll-bottom="messages">
            <ul>
                <li v-for="item in messages">
                    <p class="time">
                        <span>{{ item.time | time }}</span>
                    </p>
                    <div :class="{ self: item.self }">
                        <img class="avatar" width="30" height="30" :src="item.self ? userImageUrl : friend.imageUrl"/>
                        <div class="text">{{ item.content }}</div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    import DateUtil from "../../../libs/DateUtil";

    export default {
        data () {
            return {
                friend: {}
            };
        },
        watch: {
            '$route': 'selectFriend'
        },
        methods: {
            selectFriend () {
                this.friendList.map(item => {
                    if (item.id === this.currentFriendId) {
                        this.friend = item;
                    }
                });
            },
        },
        filters: {
            // 将日期过滤为 hour:minutes
            time (date) {
                if (typeof date === 'string') {
                    date = new Date(date);
                } else {
                    date = new Date();
                }
                let dateString = DateUtil.formatDate(date, "yyyy-MM-dd hh:mm:ss");
                let today = DateUtil.formatDate(new Date(), "yyyy-MM-dd") + " 00:00:00";
                if (new Date(dateString) < new Date(today)) {
                    return DateUtil.formatDate(date, "yyyy-MM-dd hh:mm:ss");
                } else {
                    let minutes = date.getMinutes();
                    if (minutes < 10) {
                        minutes = '0' + minutes.toString();
                    }
                    return date.getHours() + ':' + minutes;
                }
            }
        },
        directives: {
            // 发送消息后滚动到底部
            'scroll-bottom': {
                update (el, binding, nodeDom, oldVnode) {
                    // 指令的定义
                    nodeDom.context.$nextTick(() => {
                        el.scrollTop = el.scrollHeight - el.clientHeight;
                    });
                }
            },
        },
        computed: {
            messages () {
                return this.$store.state.messages;
            },
            userImageUrl () {
                return this.$store.state.user.userIdentity.imageUrl;
            },
            friendList () {
                return this.$store.state.user.friends;
            },
            currentFriendId () {
                return this.$store.state.currentFriendId;
            }
        },
        created () {
            this.selectFriend();
        }
    };
</script>