<template>
    <div :style="{flex:1}">
        <header-nav @headerEvent="headerEvent" :title="id ? '今日入库' : '今日扫描'" right-button />
        <inputFrame @select="select" style="margin-top:20px;margin-bottom:20px;" today="today"></inputFrame>
        <scroller class="contextList">
            <div class="list" v-for="(item, i) in listData" :key="i" @click="toDetail(i)">
                <div class="list_option">
                    <text class="tit">{{item.company}}</text>
                    <text class="content">{{item.orderId}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">货号</text>
                    <text class="content">{{item.shelves}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">状态</text>
                    <text class="content">{{item.orderStatus*1 === 1 ? '已入库' : '已出库'}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">客户电话</text>
                    <text class="content">{{item.expressOwnerTel}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">入库时间</text>
                    <text class="content">{{item.handleTime}}</text>
                </div>
                <text class="detailInfo">&#xe727;</text>
            </div>
        </scroller>
    </div>
</template>

<script>
    import inputFrame from "../../inform/uninformedType/input.vue";
    import baseStyle from "../../../../common/baseStyle.js";
    // import findInformData from '../../inform/findInformData';

    export default {
        components: { inputFrame },
        data() {
            return {
                id: true,
                baseStyle,
                listData: [],
                oldLIstData: []
            };
        },
        methods: {
            headerEvent() {},
            select(value){
                let listData = this.oldLIstData;
                let newObj = []
                for (let i = 0; i < listData.length; i++) {
                    if (value === listData[i].company
                        || value === listData[i].shelves
                        || value === listData[i].expressOwnerTel) {
                        newObj.push(listData[i])
                    }
                }
                this.listData = newObj;
                this.toast(newObj.length ? '查询成功' : '未查找到数据');
            },
            // 跳转到详情页
            toDetail(i){
                let data = this.listData[i];
                this.push('pages/page_lzj/index/todayInventory/detail', {data})
            }
        },
        async created(){
            let data = await new Promise(resolve=>{
                this.fetch({
                    url: '/manager/order/getScan',
                    body: {}
                }, result => {
                    let arr = result.res.data;
                    arr.sort((a, b)=>{
                        return a.orderStatus*1 - b.orderStatus*1
                    });
                    resolve(arr)
                })
            });

            this.oldLIstData = data;
            this.listData = data;

            // 身份
            this.id = await this.verdictId();
        }
    };
</script>

<style scoped>
    .contextList{
        flex: 1;
        width: 750px;
        padding-left: 30px;
        padding-right: 30px;
        padding-bottom: 30px;
    }
    .list{
        flex-direction: column;
        width: 690px;
        height: 324px;
        padding-left: 20px;
        padding-right: 30px;
        margin-bottom: 40px;
        border-width: 2px;
        border-style: solid;
        border-color: rgba(230,230,230,1);
        border-radius:10px;
    }
    .list_option{
        flex: 1;
        flex-direction: row;
        align-items: center;
    }
    .tit,
    .content{
        font-size:28px;
        color:rgba(102,102,102,1);
    }
    .tit{
        width: 200px;
    }
    .detailInfo{
        position: absolute;
        right: 0;
        height: 324px;
        padding-right: 40px;
        color: #666;
        font-size: 70px;
        font-family: iconfont;
        line-height: 324px;
    }
</style>
