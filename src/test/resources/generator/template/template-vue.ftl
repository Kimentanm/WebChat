<#--noinspection ALL-->
<!-- ${author} created on ${date}-->
<style lang="less">
    @import '../../styles/common.less';

</style>

<template>
    <div class="thng-crud-container">

        <div class="thng-crud-top">
            <Row>
                <Col span="24">

                <Button class="" type="primary" @click="add">添加</Button>


                <Input class="pull-right" v-model="searchModel" icon="ios-search" placeholder="搜索..."
                       style="width: 200px"></Input>
                </Col>
            </Row>
        </div>
        <br>
        <div class="thng-crud-bottom">
            <Row>
                <Col span="24">
                <Table :loading="loading" :columns="columns" :data="data"></Table>

                <div style="margin: 10px;overflow: hidden">
                    <div style="float: right;">
                    <#--<Page :total="pageInfo.total" :current="pageInfo.start" show-total @on-change="changePage"></Page>-->
                        <Page v-model="pageInfo"
                              :current="pageInfo.pageNum"
                              :total="pageInfo.total"
                              :page-size="pageInfo.pageSize"
                              size="small"
                              :page-size-opts="[5,10,15]"
                              @on-change="changePage"
                              @on-page-size-change="changePageSize"
                              show-elevator
                              show-sizer
                              show-total></Page>
                    </div>
                </div>
                </Col>
            </Row>
        </div>

        <Modal
                v-model="editModal"
                title="编辑${name}">

        ${EditForm}
            <Spin fix v-if="editLoading">
                <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                <div>加载中...</div>
            </Spin>
        </Modal>

        <Modal v-model="deleteModal" width="360">
            <p slot="header" style="color:#f60;text-align:center">
                <Icon type="information-circled"></Icon>
                <span>删除${name}</span>
            </p>
            <div style="text-align:center">
                <p>删除该${name}，将无法恢复！</p>
                <p>是否删除?</p>
            </div>
            <div slot="footer">
                <Button type="error" size="large" long :loading="isDeleting" @click="delete${name}">删除</Button>
            </div>
        </Modal>


    </div>
</template>
<script>
    export default {
        data() {
            return {
                searchModel: undefined,
                ${name}Form: ${editFormData},
                ${name}FormRule: ${editFormDataRule},
                loading: false,
                isSaving: false,
                isDeleting: false,
                pageInfo: {},
                editModal: false,
                deleteModal: false,
                deleteIndex: '',
                columns: ${columns},
                data: [],
                editLoading: false
            }
        },
        methods: {
            get${name}List() {
                this.loading = true;
                const self = this;
                const params = {
                    page: this.pageInfo.pageNum || 1,
                    size: this.pageInfo.pageSize || 10
                };
                this.$api.get('/${name}', {params: params}).then((res) => {
                    self.loading = false;
                    if (res.data.code === 200) {
                        const result = res.data && res.data.data;
                        self.data = result && result.list;
                        self.pageInfo.total = result && result.total;
                    } else {
                        self.$Message.error('获取数据失败！' + res.data.code);
                    }

                })
            },
            reload${name}List() {
                this.pageInfo.pageNum = 1;
                this.get${name}List();
            },

            changePage(currentPage) {
                this.pageInfo.pageNum = currentPage;
                this.get${name}List();
            },

            changePageSize(pageSize) {
                this.pageInfo.pageSize = pageSize;
                this.get${name}List();
            },
            add() {
                this.isSaving = false;
                this.${name}Form = ${editFormData};
                this.$refs.${name}Form.resetFields();
                this.editModal = true;
            },

            edit(index) {
                this.isSaving = false;
                this.editLoading = true;
                const self = this;
                this.$refs.${name}Form.resetFields();
                this.editModal = true;
                this.$api.get('/${name}/' + self.data[index].id, {}).then((res) => {
                    if (res.data.code === 200) {
                        self.${name}Form = res.data.data;
                    } else {
                        self.$Message.error('获取${name}失败！' + res.data.code);
                    }
                    self.editLoading = false;
                });
            },

            handleSubmit() {
                this.isSaving = true;
                const self = this;

                this.$refs.${name}Form.validate((valid) => {
                    if (valid) {
                        if (this.${name}Form.id) {
                            this.$api.put('/${name}', self.${name}Form).then((res) => {
                                if (res.data.code === 200) {
                                    self.isSaving = false;
                                    self.editModal = false;
                                    self.reload${name}List();
                                    self.$Message.success('更新成功！');
                                } else {
                                    self.$Message.error('更新失败！' + res.data.code);
                                }

                            });
                        } else {
                            this.$api.post('/${name}', self.${name}Form).then((res) => {
                                if (res.data.code === 200) {
                                    self.isSaving = false;
                                    self.editModal = false;
                                    self.reload${name}List();
                                    self.$Message.success('添加成功！');
                                } else {
                                    self.$Message.error('添加失败！' + res.data.code);
                                }
                            })
                        }

                    } else {
                        self.isSaving = false;
                    }
                })
            },
            handleReset() {
                this.editModal = false;
                console.log('handleReset');
            },
            remove(index) {
                this.deleteModal = true;
                this.deleteIndex = index;
                this.isDeleting = false;
            },
            delete${name}() {
                this.isDeleting = true;
                const self = this;
                this.$api.delete('/${name}/' + self.data[self.deleteIndex].id, {}).then((res) => {
                    if (res.data.code === 200) {
                        self.isDeleting = false;
                        self.deleteModal = false;
                        self.reload${name}List();
                        self.$Message.success('删除成功！');
                    } else {
                        self.$Message.error('删除失败！' + res.data.code);
                    }

                });

            }

        },
        created() {
            this.get${name}List();
        }
    }
</script>