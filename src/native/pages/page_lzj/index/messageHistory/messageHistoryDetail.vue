<template>
    <div>
        <header-nav @headerEvent="headerEvent" title="SMS" right-button />
        <scroller class="scroller">
            <div v-for="(item, i) in smsList" :key="i">
                <div class="timer">
                    <text class="timer_text">{{item.smsTime.substr(5, 2) + '月' + item.smsTime.substr(8, 2) + '日'}}</text>
                    <text class="timer_text">{{'下午' + item.smsTime.substr(11, 5)}}</text>
                </div>
                <div class="smsContext">
                    <text class="timer_text smsContext_tit" style="color: #333333;">{{item.smsContent.substr(0, 7)}}</text>
                    <text class="timer_text smsContext_content">{{
                        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                        item.smsContent.substr(8, item.smsContent.length - 1)
                        }}</text>
<!--                    <text class="takeACode">{{item.smsContent.match(/取件码[0-9]{8}/)[0].substr(3, 8)}}</text>-->
                </div>
            </div>
        </scroller>
    </div>
</template>

<script>
    export default {
        data(){
          return {
              smsList: []
          }
        },
        created() {
            let globalEvent = weex.requireModule('globalEvent');
            globalEvent.addEventListener("onPageInit", (param) => {
                this.fetch({
                    url: 'orderSms/selectsms',
                    body: {
                        phone: param.phone
                    }
                }, result => {
                    this.smsList = result.res.data;
                })
            });
        }
    }
</script>

<style scoped>
.scroller{
    flex: 1;
    padding-left: 30px;
    padding-top: 10px;
}
.timer{
    flex-direction: row;
    margin-bottom: 20px;
}
.timer_text{
    color: #999999;
    font-size: 24px;
}
.smsContext{
    position: relative;
    flex-direction: row;
    width: 650px;
    padding-left: 8px;
    padding-top: 26px;
    padding-bottom: 18px;
    margin-bottom: 38px;
    background-color: #e6e6e6;
    border-radius: 10px;
}
.smsContext_tit{
    position: absolute;
    top: 32px;
    left: 4px;
}
.smsContext_content{
    width: 650px;
    line-height: 40px;
}
.takeACode{
    position: absolute;
    top: 72px;
    left: 184px;
}
</style>