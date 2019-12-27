<template>
    <div class="wrap" :style="{height: dh + 'px'}">
        <div class="strandedInA">
            <text class="strandedInA_cancel" @click="strandedInAClick('')">&#xe613;</text>
            <text class="strandedInA_title">滞留件提示</text>
            <text class="strandedInA_context">您有{{StrandedInANumber}}个长达48小时的滞留件，请及时处理</text>
            <div class="strandedInA_footer">
                <text class="strandedInA_footer_text" @click="strandedInAClick('')">取消</text>
                <text class="strandedInA_footer_text strandedInA_footer_text2"
                      @click="strandedInAClick('pages/page_lm/retention/retention')">现在去通知</text>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "strandedInA",
        data(){
            return {
                dh: 0
            }
        },
        created() {
            this.dh = this.getScreenHeight();
        },
        props: ['StrandedInANumber'],
        methods: {
            async strandedInAClick(url){
                // 父组件更新为已查看
                this.$emit('strandedInAClick');

                // 更改为已查看
                this.fetch({
                    url: '/manager/order/cancel',
                    body: {}
                }, result => {});


                if (url) {
                    this.push(url);
                }
            }
        }
    }
</script>

<style scoped>
    .wrap{
        justify-content: center;
        align-items: center;
        position: fixed;
        width: 750px;
        background-color:rgba(0,0,0,0.2);
    }
    .strandedInA{
        align-items: center;
        width: 690px;
        height: 400px;
        background-color: #fff;
        opacity: 1;
    }
    .strandedInA_cancel{
        width: 694px;
        padding: 28px;
        color: #ccc;
        font-family: iconfont;
        text-align: right;
    }
    .strandedInA_title{
        padding-top: 16px;
        padding-bottom: 40px;
        color: #333;
        font-size: 34px;
        text-align: center;
    }
    .strandedInA_context{
        width: 378px;
        color: #333;
        font-size: 28px;
        text-align: center;
    }
    .strandedInA_footer{
        flex-direction: row;
        position: absolute;
        bottom: 0;
        width: 750px;
        height: 88px;
        border-top-width: 1px;
        border-top-style: solid;
        border-top-color: #ccc;
    }
    .strandedInA_footer_text{
        width: 375px;
        color: #999;
        font-size: 34px;
        text-align: center;
        line-height: 88px;
    }
    .strandedInA_footer_text2{
        border-left-width: 1px;
        border-left-style: solid;
        border-left-color: #ccc;
        color: #0077E2;
    }
</style>