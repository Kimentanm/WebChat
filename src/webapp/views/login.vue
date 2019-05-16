<style lang="less">
    @import './login.less';

    .login-card > .ivu-card-body {
        padding: 0px;
    }

    .login-button {
        height: 45px;
        text-align: center;
        background-color: #19be6b;
        &-text {
            font-size: 20px;
            color: #FFFFFF;
            position: relative;
            top: 30%;
            transform: translateY(-50%);
        }
    }

</style>

<template>
    <div class="login" @keydown.enter="handleSubmit">
        <div class="login-con">
            <span style="font-size: 35px;color: #FFF">Web聊天室</span>
            <Card class="login-card" :bordered="false" style="margin-top: 40px;border-radius: 3%">
                <p slot="title">欢迎登录</p>
                <img src="http://img.kimen.xyz/psb.png"/>
                <div class="form-con">
                    <Form ref="loginForm" :model="form" :rules="rules" style="padding: 16px 16px 0 16px">
                        <FormItem prop="userName">
                            <Input v-model="form.userName" placeholder="请输入用户名">
                            <span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                        <FormItem prop="password">
                            <Input type="password" v-model="form.password" placeholder="请输入密码">
                            <span slot="prepend">
                                    <Icon :size="14" type="locked"></Icon>
                                </span>
                            </Input>
                        </FormItem>
                    </Form>
                    <div style="margin-top: -8px;margin-bottom: 6px">
                        <span style="font-size: 10px">还没有账号？<a @click="startRegister">点击注册</a></span>
                    </div>
                    <a>
                        <div @click="handleSubmit" slot="footer" class="login-button">
                            <span class="login-button-text">{{loginButtonTest}}</span>
                        </div>
                    </a>
                </div>
            </Card>
        </div>

        <Modal
                v-model="registerModal"
                width="330"
                :styles="{top: '20px'}"
                title="用户注册">
            <Form ref='userForm' :model='userForm' :rules='userFormRule' :label-width='90' style="width: 450px">
                <FormItem label='登录名' prop='loginName'>
                    <Input v-model='userForm.loginName' :maxlength=30 placeholder='请输入登录名' style="width: 170px"/>
                </FormItem>
                <FormItem label='昵称' prop='name'>
                    <Input v-model='userForm.name' :maxlength=50 placeholder='请输入昵称' style="width: 170px"/>
                </FormItem>
                <FormItem label='密码' prop='password'>
                    <Input type="password" v-model='userForm.password' :maxlength=50 placeholder='请输入密码,至少6个字符'
                           style="width: 170px"/>
                </FormItem>
                <FormItem label='联系方式' prop='mobile'>
                    <Input v-model='userForm.mobile' :maxlength=50 placeholder='请输入联系方式' style="width: 170px"/>
                </FormItem>
                <FormItem label='电子邮箱' prop='email'>
                    <Input v-model='userForm.email' :maxlength=50 placeholder='请输入电子邮箱' style="width: 170px"/>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="ghost" @click="handleReset()" style="margin-left: 8px">取消</Button>
                <Button type="success" @click="handleRegister()">{{registerButtonText}}</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    export default {
        data () {
            return {
                form: {
                    userName: '',
                    password: ''
                },
                loginButtonTest: '登录',
                registerButtonText: '注册',
                rules: {
                    userName: [
                        {required: true, message: '账号不能为空', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'}
                    ]
                },
                // 注册
                registerModal: false,
                userForm: {
                    loginName: undefined,
                    name: undefined,
                    password: undefined,
                    mobile: undefined,
                    email: undefined,
                },
                userFormRule: {
                    loginName: [
                        {required: true, message: '登录名不能为空.', trigger: 'blur'},
                        {type: 'string', max: 30, message: '登录名最多30字符', trigger: 'blur'},
                    ],
                    name:
                        [
                            {required: true, message: '名称不能为空.', trigger: 'blur'},
                            {type: 'string', max: 50, message: '名称最多50字符', trigger: 'blur'},
                        ],
                    password:
                        [
                            {required: true, message: '密码不能为空.', trigger: 'blur'},
                            {min: 6, message: '请至少输入6个字符', trigger: 'blur'},
                            {max: 32, message: '最多输入32个字符', trigger: 'blur'}
                        ],
                    mobile: [
                        {type: 'string', max: 30, message: '联系方式必须为数字', trigger: 'blur'}
                    ],
                    email: [
                        {type: 'email', message: '电子邮箱的格式不正确', trigger: 'blur'}
                    ],
                },
            };
        },
        methods: {
            handleSubmit() {
                let self = this;
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        self.loginButtonTest = '正在登陆...'
                        self.logining = true;
                        self.$store.dispatch('login', self.form).then(() => {
                            self.logining = false;
                            self.loginButtonTest = '登陆成功';
                            self.$router.push({
                                name: 'home_index'
                            });
                        }).catch((err) => {
                            self.loginButtonTest = '登录';
                            console.log('>>> login failed', err);
                            self.$Message.error('用户名或密码错误', 8000);
                            self.logining = false;
                        });
                    }
                });
            },
            handleReset () {
                this.registerModal = false;
                this.userForm = {
                    loginName: undefined,
                    name: undefined,
                    password: undefined,
                    mobile: undefined,
                    email: undefined
                };
                this.$refs.userForm.resetFields();
            },
            startRegister () {
                this.registerModal = true;
            },
            handleRegister () {
                let self = this;
                this.$refs.userForm.validate((valid) => {
                    if (valid) {
                        this.registerButtonText = '正在注册';
                        this.$api.post('/user', self.userForm).then(function (res) {
                            if (res.data.code === 200) {
                                self.registerButtonText = '注册成功';
                                self.$Message.success('注册成功！请快点登录吧~~');
                                self.form.userName = self.userForm.loginName;
                                self.form.password = self.userForm.password;
                                self.handleReset();
                            } else if (res.data.code === 400) {
                                self.$Message.error(res.data.message);
                            } else {
                                self.$Message.error('注册失败，请重新注册');
                            }
                            self.registerButtonText = '注册';
                        });
                    }
                });
            },
        },
    };
</script>

<style>

</style>
