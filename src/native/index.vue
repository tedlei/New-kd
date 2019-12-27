<template>
    <div style="flex:1;">
        <slider style="flex: 1;"
                @change="change"
                :style="{height: dh + 'px'}"
                interval="3000"
                :auto-play="false"
                :index="indexPage"
                :infinite="false">
            <index></index>
            <mine></mine>
        </slider>
        <footer-nav ref="footer" @footerEvent="footerEvent"/>
        <strandedInA v-if="StrandedInANumber"
                     :StrandedInANumber="StrandedInANumber"
                     @strandedInAClick="strandedInAClick"></strandedInA>

    </div>
</template>

<script>
    import index from "./pages/page_lzj/index/index.vue";
    import mine from "./pages/page_lzj/mine/mine.vue";
    import backKeyEvent from "./common/backKeyEvent";   // 添加返回键拦截  并连接点击退出应用
    import strandedInA from "./component/component_lzj/strandedInA.vue";    // 滞留件弹窗

    export default {
        components: { index, mine, strandedInA },
        data() {
            return {
                indexPage: 0, // 首页还是我的页面
                dh: 0,
                StrandedInANumber: 0  // 滞留件数量
            };
        },
        mounted() {
            this.dh = this.getScreenHeight();
        },
        methods: {
            footerEvent(val) {
                this.indexPage = val ? 0 : 1;
            },
            change(event) {
                let index = event.index;
                this.indexPage = index;
                this.$refs.footer.changeBool(index !== 1);
            },
            // 滞留件点击事件
            strandedInAClick(){
                // 将滞留件数量重新设置为0, 更新为已查看, 不再提示弹窗
                this.StrandedInANumber = 0;
            }
        },
        async created() {
            // 设置android baseUrl

            let  scanerModule = weex.requireModule('ScanerModule');
            scanerModule.initBaseUrlOrBaseCom(await this.getBaseUrl());

            // 初始化微信模块
            let wechat=weex.requireModule('wechat');
            let appId="com.farwolf.express";
            wechat.regist(appId);

            // 接收滞留件结果
            let globalEvent = weex.requireModule('globalEvent');
                globalEvent.addEventListener("onPageInit", (param) => {
                    let num = param.number;
                    this.StrandedInANumber = num;
                    if(num) this.audio("retention", "retentionHint");
                });

            await backKeyEvent.androidEnableExit.call(this, (page)=>{
                if (this.indexPage === 1) {
                    this.indexPage = 0
                } else {
                    let currentDate = new Date();
                    // 点击提示退出 间隔超过2s退出
                    if (currentDate - this.beginDate > 2000) {
                        this.toast("再按一次退出");
                        this.beginDate = currentDate;
                    } else page.exit()
                }
            });
        }
    };
</script>

<style scoped></style>
