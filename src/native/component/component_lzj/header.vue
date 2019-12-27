<template>
    <div style="background-color:#0677e6;">
        <div class="header"></div>
        <wxc-minibar
                background-color="#0677e6"
                :use-default-return="false"
                @wxcMinibarLeftButtonClicked="minibarLeftButtonClick"
                @wxcMinibarRightButtonClicked="minibarRightButtonClick">
            <text class="iconfont icon" slot="left">&#xe837;</text>
            <text style="font-size: 44px;color: #fff;" slot="middle">{{title}}</text>
            <text class="icon right" slot="right">{{rightButton}}</text>
        </wxc-minibar>
    </div>
</template>

<script>
    import { WxcMinibar } from "weex-ui";
    const modal = weex.requireModule("modal");

    // 向外部通信
    // const headerEvent = new BroadcastChannel('headerEvent')

    export default {
        components: { WxcMinibar },
        methods: {
            minibarLeftButtonClick() {
                this.$emit('headerEvent', 'left');
                // headerEvent.postMessage('left')
                if (this.back === undefined || this.back) {
                    let param = this.param;
                    if (param === undefined) {
                        this.pop()
                    } else this.pop(param)
                }
            },
            minibarRightButtonClick() {
                this.$emit('headerEvent', 'right')
                // headerEvent.postMessage('right')
            }
        },
        props: ['title', 'right-button', 'back', 'param']
    };
</script>

<style scoped>
    .header{
        height: 40px;
        background-color: #0677e6;
    }
    .iconfont{
        font-family: iconfont;
        font-size: 40px;
    }
    .icon{
        height:90px;
        min-width:100px;
        color: #fff;
        line-height:100px;
    }
    .right{
        font-size: 28px;
        text-align:right;
    }
</style>
