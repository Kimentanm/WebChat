<style lang="less">
    @import "./main.less";

    .logo-con-text {
        color: #000000;
        display: inline-block;
        position: absolute;
        top: 50%;
        transform: translateY(-60%);
        margin-left: 10px;
    }

    .app {
        margin: 10px auto;
        width: 800px;
        height: 98%;

        overflow: hidden;
        border-radius: 3px;
    }
</style>
<template>
    <div class="main" :class="{'main-hide-text': hideMenuText}">
        <div class="main-header-con" style="padding-left: 0px">
            <div class="main-header">
                <div class="logo-con">
                    <div style="margin-left: 35px">
                        <img src="../images/logo_chat.svg" key="max-logo"/>
                        <span class="logo-con-text">Web Chat</span>
                    </div>
                </div>
                <!--<div class="header-middle-con">-->
                    <!--<div class="main-breadcrumb">-->
                        <!--<breadcrumb-nav :currentPath="currentPath"></breadcrumb-nav>-->
                    <!--</div>-->
                <!--</div>-->
                <div class="header-avator-con">
                    <div class="user-dropdown-menu-con">
                        <Row type="flex" justify="end" align="middle" class="user-dropdown-innercon">
                            <Dropdown transfer trigger="click" @on-click="handleClickUserDropdown">
                                <a href="javascript:void(0)">
                                    <span class="main-user-name">{{ computedUserName }}</span>
                                    <Icon type="arrow-down-b"></Icon>
                                </a>
                                <DropdownMenu slot="list">
                                    <DropdownItem name="ownSpace">个人中心</DropdownItem>
                                    <DropdownItem name="loginout" divided>退出登录</DropdownItem>
                                </DropdownMenu>
                            </Dropdown>
                            <Avatar :src="avatorPath" style="background: #619fe7;margin-left: 10px;"></Avatar>
                        </Row>
                    </div>
                </div>
            </div>
        </div>
        <div class="app">
            <div class="sidebar-menu-con"
                 :style="{width: hideMenuText?'60px':'200px', overflow: hideMenuText ? 'visible' : 'auto', background: '#2e3238'}">
                <sidebar-menu :menuList="menuList"/>
            </div>
            <div class="single-page-con" :style="{left: hideMenuText?'60px':'200px'}" style="background-color: #eee;">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>
<script>
    import sidebarMenu from './main_components/sidebarMenu.vue';
    import Cookies from 'js-cookie';
    import util from '../libs/util.js';

    export default {
        components: {
            sidebarMenu,
        },
        data () {
            return {
                spanLeft: 4,
                spanRight: 20,
                currentPageName: '',
                hideMenuText: false,
                showFullScreenBtn: window.navigator.userAgent.indexOf('MSIE') < 0,
                messageCount: 0,
                lockScreenSize: 0
            };
        },
        computed: {
            computedUserName () {
                return this.$store.state.user.userIdentity.name;
            },
            menuList () {
                return this.$store.state.menuList;
            },
            tagsList () {
                return this.$store.state.tagsList; // 所有页面的页面对象
            },
            pageTagsList () {
                return this.$store.state.pageOpenedList; // 打开的页面的页面对象
            },
            currentPath () {
                return this.$store.state.currentPath; // 当前面包屑数组
            },
            avatorPath () {
                return this.$store.state.user.userIdentity.imageUrl;
            },
            cachePage () {
                return this.$store.state.cachePage;
            },
            lang () {
                return this.$store.state.lang;
            },
            isFullScreen () {
                return this.$store.state.isFullScreen;
            }
        },
        methods: {
            init () {
                this.$store.commit('setCurrentPageName', this.$route.name);
                let messageCount = 3;
                this.messageCount = messageCount.toString();
                if (this.$route.path === '/') {
                    util.openNewPage(this, 'home_index');
                    this.$router.push({
                        name: 'home_index'
                    });
                }
            },
            handleClickUserDropdown (name) {
                if (name === 'ownSpace') {
                    util.openNewPage(this, 'ownspace_index');
                    this.$router.push({
                        name: 'ownspace_index'
                    });
                } else if (name === 'loginout') {
                    // 退出登录
                    this.$store.dispatch('logout').then(resp => {
                        this.disconnetWM();
                        this.$Notice.close('greeting');
                        this.$router.push({
                            name: 'login'
                        });
                    });
                }
            },
            handleFullScreen () {
                this.$store.commit('handleFullScreen');
                // this.$store.commit('changeFullScreenState');
            },
            checkTag (name) {
                let openpageHasTag = this.pageTagsList.some(item => {
                    if (item.name === name) {
                        return true;
                    }
                });
                if (!openpageHasTag) { // 解决关闭当前标签后再点击回退按钮会退到当前页时没有标签的问题
                    util.openNewPage(this, name, this.$route.params || {}, this.$route.query || {});
                }
            }
        },
        watch: {
            '$route' (to) {
                this.$store.commit('setCurrentPageName', to.name);
                let pathArr = util.setCurrentPath(this, to.name);
                if (pathArr.length > 2) {
                    this.$store.commit('addOpenSubmenu', pathArr[1].name);
                }
                this.checkTag(to.name);
            },
        },
        mounted () {
            this.init();
        },
        created () {
            // 显示打开的页面的列表
            this.$store.commit('setOpenedList');
        }
    };
</script>
