<template>
    <div class="wrap">
        <div class="icon" :style="fCenter" @click="toScan">
            <text class="iconfont" :style="{color: '#0077e2',fontSize: '54px'}">&#xe615;</text>
        </div>
        <div class="footer" :style="fdr" @click="footerEvent('/index')">
            <div class="fonter_list" :style="fx1">
                <text class="iconfont" :style="{color: bool ? '#0077e2' : '#999', fontSize: '58px'}">{{bool ? '&#xe63f' : '&#xe644'}}</text>
                <text>首页</text>
            </div>
            <div class="fonter_list" :style="fx1" @click="toScan">
                <text style="font-size: 28px;">{{context}}</text>
            </div>
            <div class="fonter_list" :style="fx1" @click="footerEvent('/mine')">
                <text class="iconfont" :style="{color: bool ? '#999' : '#0077e2', fontSize: '58px'}">{{bool ? '&#xe645' : '&#xe636'}}</text>
                <text>我的</text>
            </div>
        </div>
    </div>
</template>

<script>
    import baseStyle from "../../common/baseStyle.js";
    let { fdr, fx1, fCenter } = baseStyle;

    export default {
        data() {
            return {
                // 样式
                id: true,   // 身份识别
                userInfo: [], // 用户信息
                fdr,
                fx1,
                fCenter,
                bool: true,
                context: ''
            };
        },
        methods: {
            footerEvent(url){
                this.bool = url !== '/mine';
                this.$emit('footerEvent', this.bool)
            },
            changeBool(val){this.bool = val},
            // 跳转到扫码界面
            toScan(){
                let url = '', // 页面地址
                    params = [],  // 传递参数
                    {id, userInfo} = this;
                if (this.id) {
                    // 代办点
                    url = 'openScanerActivity';
                    // 传递参数 地址
                    params.push(userInfo.addres);
                } else {
                    // 快递员
                    url = 'openExScanerActivity';
                    // 传递参数 姓名 电话
                    params.push(userInfo.userName);
                    params.push(userInfo.userPhon);
                }
                params.push(userInfo.sessionId);
                this.pushAndroid(url, params);
            }
        },
        async created() {
            let context = '扫描';
            // 身份判断
            if (await this.verdictId()) context = '入库';
            this.context = context;

            // 获取身份id
            this.id = await this.verdictId();

            // 获取用户信息
            this.getItem('userInfo', result=>{
                this.userInfo = result.data;
            })
        }
    };
</script>

<style scoped>
    .wrap{
        justify-content: flex-end;
        height: 138px;
    }
    .icon{
        left: 323px;
        top: 65px;
        width: 104px;
        height: 104px;
        border-style: solid;
        border-width: 4px;
        border-color: #0077e2;
        border-radius: 52px;
    }
    .footer{
        height: 98px;
        box-shadow:0px 0px 10px 0px rgba(0, 0, 0, 0.2);
    }
    .fonter_list{
        align-items: center;
        justify-content: flex-end;
    }
    .iconfont{
        font-family: iconfont;
    }

</style>
