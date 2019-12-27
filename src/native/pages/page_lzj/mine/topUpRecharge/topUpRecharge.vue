<template>
  <div style="flex:1;">
    <headerNav title="充值" rightButton></headerNav>
    <div style="background-color: #f2f2f2;">
      <div class="username">
        <text style="color: #333;font-size: 44px;">{{userPhone}}</text>
        <text style="margin-top: 10px;">默认用户名</text>
      </div>
    </div>
    <div class="content">
      <text style="padding-bottom: 18px;color:#999;font-size: 28px;">充值金额</text>
      <div style="flex-direction: row;flex-wrap: wrap;">
        <div v-for="(item, n) in money" :key="n" style="position:relative;">
          <text class="money" :class="[num === n ? 'checked' : '']"
                :style="{marginRight: (n+1)%3 === 0 ? '0' : '28px' }"
                :key="n"
                ref="input1"
                @click="cutChenked(n)">{{item.number}}元</text>
          <div class="start" v-if="n !== 0 && n !== 1 && userInfo.userFirst === '1'">
            <text class="start_text">首充</text>
          </div>
          <text class="money_text" v-if="userInfo.userFirst === '1'">{{item.text}}</text>
        </div>
        <div style="flex-direction: row;align-items: center;width: 690px;padding-top: 34px;">
          <text style="color: #333;font-size: 34px;margin-right: 20px;">其它金额</text>
          <label>
            <input
              v-model="moneyNum"
              class="inMoney"
              type="text"
              placeholder="请输入其它金额"
              :maxlength="4"
              @change="changeMoneyNum"
              @keyboard="keyboards">
          </label>
          <text  style="color: #333;font-size: 34px;margin-left: 20px;">元</text>
        </div>
        <div class="foot">
          <text style="color: #999;font-size: 32px;margin-bottom:40px;">充值方式</text>
          <div class="foot_content">
            <div class="mode_of_payment" @click="cutmoney(true)" :style="{borderColor: payType ? '#0077e2' : '#B3B3B3'}">
              <text style="margin-right: 20px;color: #6bcc03;font-size: 62px;font-family: iconfont;">&#xe607;</text>
              <text style="color: #333;font-size: 34px;">微信支付</text>
              <div v-if="payType" class="zfChecked">
                <text class="zfChecked_text">&#xe60b;</text>
              </div>
            </div>
            <div class="mode_of_payment" @click="cutmoney(false)"
                 :style="{borderColor: !payType ? '#0077e2' : '#B3B3B3'}">
              <text style="margin-right: 20px;color: #06b4fd;font-size: 62px;font-family: iconfont;">&#xe623;</text>
              <text style="color: #333;font-size: 34px;">支付宝支付</text>
              <div v-if="!payType" class="zfChecked">
                <text class="zfChecked_text">&#xe60b;</text>
              </div>
            </div>
          </div>
           <text class="czbt"
                 :class="[moneyNumCorrect ? 'czbtSelect' : '']"
                 @click="goMoney">充值</text>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const stream = weex.requireModule('stream') || {};

export default {
  data() {
    return {
      oldNum: 0,
      num: 0, // 当前选中金额
      money: [
        {
          number: 30,
          text: ''
        },
        {
          number: 50,
          text: ''
        },
        {
          number: 100,
          text: '送100条短信'
        },
        {
          number: 200,
          text: '送300条短信'
        },
        {
          number: 500,
          text: '送800条短信'
        },
        {
          number: 1000,
          text: '送2000条短信'
        }
      ],
      moneyNum: '', // 输入金额
      moneyNumCorrect: true,
      payType: true, // 默认微信  false支付宝
      userPhone: '',
      userInfo: []
    };
  },
  methods: {
    cutChenked(i){this.num = i},
    cutmoney(value){this.payType = value},
    changeMoneyNum(target){
      let value = target.value;
      if (/^(\d+(\.[0-9])?\d*)$/.test(value) && parseFloat(value) !== 0){
        this.moneyNum = value;
        if (this.num!== -1){
          this.oldNum = this.num; // 保存当前选中金额
        }
        this.num = -1;  // 验证成功则使用输入金额
      } else {
        this.num = this.oldNum;  // 验证失败使用历史选中金额
      }
    },
    keyboards(e){ // 键盘弹起收起触发
      if (!e.isShow) {
        let value = e.target.attr.value;
        if (/^(\d+(\.[0-9])?\d*)$/.test(value) && parseFloat(value) !== 0){
          this.moneyNum = value;
          if (this.num!== -1){
            this.oldNum = this.num; // 保存当前选中金额
          }
          this.num = -1;  // 验证成功则使用输入金额
        } else {
          this.num = this.oldNum;  // 验证失败使用历史选中金额
        }
      }
    },
    async goMoney(){
      let {moneyNumCorrect, money, num, moneyNum, userPhone} = this;
      if (!moneyNumCorrect) return; // 是否可支付
      // 获取金额
      if (!(/^(\d+(\.[0-9])?\d*)$/.test(moneyNum) && parseFloat(moneyNum) !== 0)){
        moneyNum = money[num].number
      }

      let url = await this.getBaseUrl();

      if (this.payType) {
        // 微信支付
        this.weixinPay(moneyNum, url);
      } else {
        // 支付宝支付
        this.alipay(moneyNum, userPhone, url)
      }
    },
    alipay(moneyNumber, userPhone, url){
      let TradeNo = new Date().getTime().toString().substr(-10, 10);
      const net = weex.requireModule('net');
      this.getItem('token', res => {
        net.post(url + 'alipay/pay',{
          TradeNo: 1 + TradeNo,
          Amount: moneyNumber,
          orderuser: userPhone
        },{
          token: res.data
        },function(res){}, (e) => {
          let p = {};
          p.signstr = e.res;
          let alipay=weex.requireModule('alipay');
          alipay.pay(p,(res)=>{
            // 支付成功
            let msg = '';
            switch (res.resultStatus) {
              case '6001':
                msg = '取消支付';
                break;
              case '9000':
                msg = '支付成功';
                break;
              default:
                msg = '操作失败'
            }
            this.toast(msg);
          });
        },function(e){},function(){});
      });
    },
    weixinPay(moneyNumber, url){
      let TradeNo = new Date().getTime().toString().substr(-10, 10);
      const net = weex.requireModule('net');
      this.getItem('token', res => {
        net.post(url + 'wxpay/apppay',{
          total_fee: moneyNumber*1,
        },{
          token: res.data
        },function(res){}, (e) => {
          let res = e.res;
          let wechat=weex.requireModule('wechat');
          let p={};

          p.appId = res.appid;
          p.partnerId = res.partnerid;
          p.prepayId= res.prepayid;
          p.packageValue = res.package;
          p.nonceStr= res.noncestr;
          p.timeStamp= res.timestamp;
          p.sign= res.sign;
          wechat.pay(p,(res)=>{
            if(res.errCode==0){
              this.toast('支付完成')
            } else this.toast('取消支付')
          })
        },function(e){},function(){});
      });
    },
    init(){
      return new Promise(resolve=>{
        this.fetch({
          url: 'orderUser/getusercode',
          body: {}
        }, res => {
          let data = res.res.data;
          this.userInfo = data;
          this.userPhone = data.userPhone;
          resolve(resolve);
        })
      })
    }
  },
  created() {
    this.init()
    this.onResume(this.init, userInfo)
  },
  watch: {
    moneyNum(value){
      if (/^(\d+(\.[0-9])?\d*)$/.test(value) && parseFloat(value) !== 0) {
        this.oldNum = this.num; // 保存当前选中金额
        this.num = -1
      } else this.num = this.oldNum
    }
  }
};
</script>

<style scoped>

.username {
  margin-top: 20px;
  height: 140px;
  padding-left: 32px;
  padding-top: 42px;
  background-color: #fff;
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
}
.content{
  position: relative;
  flex: 1;
  padding: 30px;
  background-color: #fff;
}
.start{
  justify-content: center;
  align-items: flex-end;
  position: absolute;
  top: -42px;
  left: -42px;
  width: 84px;
  height: 84px;
  background-color: #0077e2;
  transform: rotate(45deg);
}
.start_text{
  transform: rotate(-90deg);
  font-size: 18px;
  color: #fff;
}
.money_text{
  position: absolute;
  top: 80px;
  width:210px;
  color: #FF0000;
  text-align: center;
}
.money{
  width:210px;
  height:120px;
  margin-bottom: 28px;
  background-color:rgba(255,255,255,1);
  border-width:2px;
  border-style: solid;
  border-color: rgba(0,119,226,1);
  font-size: 36px;
  text-align: center;
  line-height: 120px;
  border-radius:10px;
}
.inMoney{
  position: relative;
  top: -6px;
  width: 360px;
  height: 50px;
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
  font-size: 30px;
}
.foot{
  align-items: center;
  width: 690px;
  margin-top: 86px;
}
.foot_content{
  flex-direction: row;
  justify-content: space-between;
  width: 690px;
}
.mode_of_payment{
  overflow: hidden;
  flex-direction: row;
  align-items: center;
  width:330px;
  height:90px;
  padding-left: 34px;
  background-color:#fff;
  border-width: 2px;
  border-style: solid;
  border-color: #B3B3B3;
  border-radius:10px;
}
.czbt{
  width:540px;
  height:88px;
  margin-top: 60px;
  background-color:#ccc;
  color: #fff;
  font-size: 44px;
  text-align: center;
  line-height: 88px;
  border-radius:10px;
  opacity: 1;
}
.czbt:active{
  opacity: 0.8;
}
.czbtSelect{
  background-color: #0077E2;
}
.checked{
  background-color: #0077E2;
  color: #fff;
}
.zfChecked{
  position: absolute;
  right: -31px;
  bottom: -31px;
  width: 60px;
  height: 60px;
  background-color: #0077E2;
  transform: rotate(45deg);
}
.zfChecked_text{
  position: absolute;
  top: 12px;
  left: -8px;
  color: #fff;
  font-size: 40px;
  font-family: iconfont;
  transform: rotate(-55deg)
}
</style>
