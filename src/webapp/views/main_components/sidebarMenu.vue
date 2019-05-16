<style lang="less">
    .friends-item {
        position: relative;
        top: 50%;
        transform: translateY(-50%);
        display: flex; /*Flex布局*/
        display: -webkit-flex; /* Safari */
        align-items: center; /*指定垂直居中*/
    }

    .messageManage {
        height: 60px;
        width: 100%;
        position: absolute;
        bottom: 0;
        display: flex;
        align-items: center;
        .content {
            margin-left: 90px;
            text-align: center;
            .img {
                width: 20px;
                height: 20px;
            }
            .text {
                color: #FFF;
                display: block
            }
        }
    }

    .friend-card {
        display: flex;
        align-items: center;
        height: 40px;
        width: 280px;
        margin-left: auto;
        margin-right: auto;
        margin-top: 10px;

        &-content {
            height: 40px;
            display:flex;
            align-items:center;
            .avatar {
                width: 30px;
                height: 30px
            }
            .name {
                margin-left: 10px;
                width: 145px;
                text-align: left;
                font-size: 10px;
            }
            .add-button {
                margin-left: 10px,
            }
        }

    }
</style>

<template>
    <div>
        <div>
            <Menu ref="sideMenu" :active-name="$route.name" :open-names="openedSubmenuArr" width="auto" @on-select="changeMenu" style="background: transparent">
                <template>
                    <MenuItem name="addFriend">
                        <Button icon="person-add" type="primary" long @click="addFriend">添加好友</Button>
                    </MenuItem>

                    <my-submenu name="friends" @handleClick="handleSubmenuClick">
                        <template slot="title">
                            <div style="padding: 14px 24px;display: inline">
                                <img src="../../images/friends.svg" style="height: 20px;width: auto;"/>
                                <span class="layout-text">好友列表</span>
                            </div>
                        </template>
                        <template v-for="child in friendList">
                            <MenuItem :name="child.name" :key="child.id" style="padding-left: 40px;height: 40px" v-long-click>
                                <div id="test" class="friends-item">
                                    <img style="height: 25px;width: 25px;" :src="child.icon" :key="'menuicon'+child.title"/>
                                    <span style="margin-left: 15px;color: #fff;width: 100px" :key="'menutext'+child.title">{{ child.title }}</span>
                                    <Badge :count="child.count">
                                        <div></div>
                                    </Badge>
                                </div>
                            </MenuItem>
                        </template>
                    </my-submenu>
                </template>
            </Menu>

            <div class="messageManage">
                <div class="content" @click="showMessage">
                    <a>
                        <Badge :dot="userInfo.messageUnread">
                            <img src="../../images/icon_message.svg" class="img"/>
                        </Badge>
                        <span class="text">消息</span>
                    </a>
                </div>
            </div>
        </div>

        <Modal
                v-model="messageManageModal"
                width="780"
                title="消息管理"
                style="text-align: center">
            <div>
                <Table :data="data" :columns="columns" :loading="messageLoading"></Table>
            </div>
        </Modal>

        <Modal
                v-model="addFriendModal"
                width="330"
                title="添加好友"
                style="text-align: center">
            <div>
                <div style="margin-bottom: 10px">
                    <Input v-model="userName" placeholder="请输入好友的昵称" style="width: 170px" @on-enter="search"></Input>
                    <Button type="success" style="margin-left: 20px" @click="search">查询</Button>
                </div>
                <div v-if="searchUserResult">
                    <Card class="friend-card" v-for="(item,index) in userList" :key="item.id">
                        <div class="friend-card-content">
                            <img :src="item.imageUrl" class="avatar"/>
                            <span class="name">{{item.name}}</span>
                            <Button size="small" :type="item.friend ? 'success' : 'ghost'"
                                    :icon="item.friend ? 'checkmark-round' : 'android-add'"
                                    class="add-button" @click="addOneFriend(index)" :loading="item.loading">{{item.friend ? '已发' : '添加'}}</Button>
                        </div>
                    </Card>
                </div>
                <Spin fix v-if="userLoading">
                    <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                    <div>正在查询用户...</div>
                </Spin>
            </div>
            <!--<div slot="footer" style="text-align: center">-->
                <!--<span style="font-size: 12px">当前仅为测试版，功能不完全，单方便加好友即可成功</span>-->
            <!--</div>-->
        </Modal>

        <Modal v-model="deleteFriend" width="360">
            <p slot="header" style="color:#f60;text-align:center">
                <Icon type="information-circled"></Icon>
                <span>删除好友 {{deleteUserName}}</span>
            </p>
            <div style="text-align:center">
                <p>删除该好友，您可以在添加好友中将其重新添加！</p>
                <p>是否删除?</p>
            </div>
            <div slot="footer">
                <Button type="error" size="large" long :loading="isDeleting" @click="deleteMyFriend">删除</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import util from '@/libs/util';
    import MySubmenu from './submenu/submenu';

    export default {
        components: {
            MySubmenu
        },
        data () {
            return {
                openedSubmenuArr: this.$store.state.openedSubmenuArr,
                friendList: [],
                addFriendModal: false,
                messageManageModal: false,
                userName: '',
                deleteUserName: '',
                searchUserResult: false,
                userLoading: false,
                userList: [],
                deleteFriend: false,
                isDeleting: false,
                currentFriendId: '',
                data: [],
                columns: [
                    {
                        type: 'index',
                        title: '序号',
                        width: 60,
                        align: 'center'
                    },
                    {
                        title: '发送人',
                        key: 'sender',
                        width: 100,
                        align: 'center'
                    },
                    {
                        title: '消息内容',
                        key: 'content',
                        align: 'center'
                    },
                    {
                        title: '发送时间',
                        key: 'time',
                        align: 'center',
                        width: 160
                    },
                    {
                        title: '操作',
                        type: 'handle',
                        width: 160,
                        align: 'center',
                        render: (h, params) => {
                            let result;
                            if (params.row.handleResult) {
                                let addFriendResultText;
                                if (params.row.handleResult === "1") {
                                    addFriendResultText = "通过";
                                } else {
                                    addFriendResultText = "不通过";
                                }
                                result = h('div', [
                                    h('span', {
                                        props: {
                                        },
                                        style: {
                                        },
                                        on: {
                                        }
                                    }, addFriendResultText)
                                ]);
                            } else {
                                result = h('div', [
                                    h('Button', {
                                        props: {
                                            type: 'success',
                                            size: 'small',
                                            icon: 'checkmark-round'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        },
                                        on: {
                                            click: () => {
                                                this.agree(params.index);
                                            }
                                        }
                                    }, '通过'),
                                    h('Button', {
                                        props: {
                                            type: 'error',
                                            size: 'small',
                                            icon: 'close-round'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        },
                                        on: {
                                            click: () => {
                                                this.refuse(params.index);
                                            }
                                        }
                                    }, '拒绝')
                                ]);
                            }
                            return result;
                        }
                    }
                    ],
                messageLoading: false,
            };
        },
        name: 'sidebarMenu',
        props: {
            slotTopClass: String,
            menuList: Array,
            iconSize: Number
        },
        computed: {
            tagsList () {
                return this.$store.state.tagsList;
            },
            userInfo () {
                return this.$store.state.user.userIdentity;
            }
        },
        methods: {
            changeMenu (active) {
                let array = active.split('/');
                if (array.length === 2) {
                    let name = array[0];
                    let userId = array[1];
                    if (name === 'messageBox') {
                        this.$store.state.currentFriendId = parseInt(userId);
                        this.$router.push({
                            params: {
                                userId: userId
                            },
                            name: name
                        });
                    }
                } else {
                    if (active !== 'addFriend') {
                        if (active !== 'accesstest_index') {
                            util.openNewPage(this, active);
                            this.$router.push({
                                name: active
                            });
                        }
                    }
                }
            },
            createFriendsList () {
                this.friendList = [];
                this.$store.state.user.friends.map(item => {
                    let friend = {
                        id: item.id,
                        title: item.name,
                        name: 'messageBox/' + item.id,
                        icon: item.imageUrl,
                        count: item.count
                    };
                    this.friendList.push(friend);
                });
                this.friendList.sort();
            },
            addFriend () {
                this.userLoading = false;
                this.searchUserResult = false;
                this.addFriendModal = true;
                this.userName = '';
                this.userList = [];
            },
            search () {
                this.userLoading = true;
                this.searchUserResult = true;
                let params = {
                    name: this.userName
                };
                this.$api.get('/user/search', {params: params}).then((res) => {
                    if (res.data.code === 200) {
                        this.userList = res.data.data;
                        this.userList.map(item => {
                            item.loading = false;
                        });
                    } else {
                        this.$Message.error('搜索用户失败，错误代码：' + res.data.code);
                    }
                    this.userLoading = false;
                });
            },
            addOneFriend (index) {
                let user = this.userList[index];
                if (!user.friend) {
                    user.loading = true;
                    // 通过websocket发送消息
                    let message = {
                        sender: this.userInfo.id,
                        receiver: user.id,
                        content: user.name + '你好，我想添加你为好友一起聊天！'
                    };
                    this.$stompClient.send('/sendMessage', JSON.stringify(message), {});
                    this.$api.post('/message', message).then((res) => {
                        if (res.data.code === 200) {
                            user.friend = true;
                        } else {
                            this.$Message.error('发送消息失败，错误代码：' + res.data.code);
                        }
                        user.loading = false;
                    });
                }
            },
            handleSubmenuClick () {
                this.$store.dispatch('getIdentityInfo');
            },
            deleteMyFriend () {
                this.isDeleting = true;
                this.$api.get('/user/friend/delete/' + this.currentFriendId).then((res) => {
                    if (res.data.code === 200) {
                        this.$Message.success('删除好友成功！');
                        this.deleteFriend = false;
                        if (this.currentFriendId === this.$store.state.currentFriendId) {
                            this.$router.push({
                                name: 'home_index'
                            });
                        }
                        this.$store.state.user.friends.map((item, index) => {
                            if (item.id === this.currentFriendId) {
                                this.$store.state.user.friends.splice(index, 1);
                            }
                        });
                    } else {
                        this.$Message.error('删除好友失败，错误代码：' + res.data.code);
                    }
                    this.isDeleting = false;
                });
            },
            showMessage () {
                this.messageLoading = true;
                this.messageManageModal = true;
                this.$api.get('/message/receiver/' + this.userInfo.id).then((res) => {
                    if (res.data.code === 200) {
                        this.data = res.data.data;
                        this.messageLoading = false;
                    } else {
                        this.$Message.error('获取消息列表失败，错误代码：' + res.data.code);
                    }
                });
            },
            agree (index) {
                let item = this.data[index];
                this.$api.get('/user/friend/addFriend/' + item.sender).then((res) => {
                    if (res.data.code === 200) {
                        // 通过websocket发送消息
                        let message = {
                            sender: this.userInfo.id,
                            receiver: item.sender
                        };
                        this.$stompClient.send('/addFriend', JSON.stringify(message), {});
                        this.handleAgree(item);
                    } else {
                        this.$Message.error('添加好友失败，' + res.data.message);
                        this.handleAgree(item);
                    }
                });
            },
            handleAgree (item) {
                item.handleResult = "1";
                this.$api.put("/message",item).then((res) => {
                    if (res.data.code === 200) {
                        this.data.push({});
                        this.data.pop();
                    } else {
                        this.$Message.error("通过失败，错误代码：" + res.data.code);
                    }
                });
            },
            refuse (index) {
                let item = this.data[index];
                item.handleResult = "0";
                this.$api.put("/message",item).then((res) => {
                    if (res.data.code === 200) {
                        this.data.push({});
                        this.data.pop();
                    } else {
                        this.$Message.error("通过失败，错误代码：" + res.data.code);
                    }
                });
            }
        },
        directives: {
            // 长按删除好友的指令
            'long-click': {
                update (el, binding, nodeDom, oldVnode) {
                    // 指令的定义
                    let id;
                    const self = nodeDom.context;
                    el.onmousedown = function (ev) {
                        id = setTimeout(function () {
                            self.currentFriendId = nodeDom.key;
                            self.$store.state.user.friends.map((item, index) => {
                                if (item.id === self.currentFriendId) {
                                    self.deleteUserName = item.name;
                                }
                            });
                            self.deleteFriend = true;
                        }, 1000);
                    };
                    el.onmouseup = function (ev) {
                        clearTimeout(id);
                    };
                }
            }
        },
        watch: {
            '$route' (to) {
                localStorage.currentPageName = to.name;
            },
            currentPageName () {
                this.openedSubmenuArr = this.$store.state.openedSubmenuArr;
                this.$nextTick(() => {
                    this.$refs.sideMenu && this.$refs.sideMenu.updateOpened();
                });
            },
            '$store.state.user.friends' () {
                this.createFriendsList();
            }
        },
        updated () {
            this.$nextTick(() => {
                this.$refs.sideMenu && this.$refs.sideMenu.updateOpened();
            });
        },
        created () {
            this.createFriendsList();
            this.$store.dispatch('getIdentityInfo');
        }
    };
</script>
