<template>
  <div style="flex:1;justify-content: space-between;">
    <div>
      <div style="background-color: #F2F2F2;">
        <div class="header" style="flex-direction: row;align-items: center;padding-left: 30px;padding-right: 30px;">
            <image
                style="width:160px;height:160px;margin-right: 50px;border-radius: 80px;"
                :src='id ? "root:img\/agenTpoint.png" : "root:img\/courier.png"'
                alt=""
                @click="toPage('pages/page_lzj/mine/postInformation/postInformation')"></image>
          <div style="flex: 1;">
            <text class="userInfo" style="padding-bottom: 28px;">账户：{{userInfo.userPhone}}</text>
            <text class="userInfo">昵称：{{userInfo.userName}}</text>
          </div>
          <text style="color: #fff;font-size: 60px;font-family: iconfont;"
            @click="toPage('pages/page_lzj/mine/postInformation/postInformation')">&#xe727;</text>
        </div>
        <div style="flex-direction: row;height: 206px;margin-bottom: 20px;background-color: #fff;">
          <div class="moneyLeft">
            <text class="smsNum">余额(元)</text>
            <text style="font-size: 36px;margin-bottom: 16px;">{{userInfo.userMoney}}</text>
            <text class="top_up_recharge" style="width:120px;"
              @click="toPage('pages/page_lzj/mine/topUpRecharge/topUpRecharge')">充值</text>
          </div>
          <div class="smsRight">
            <text class="smsNum">短信(条数)</text>
            <text style="font-size: 36px;margin-bottom: 16px;">{{userInfo.userSmsNum}}</text>
            <text
              class="top_up_recharge" :style="{backgroundColor: 'transparent',color:'#0077E2'}"
              @click="toPage('pages/page_lzj/mine/topUpRecharge/lookAtTheBill')">查看账单＞</text>
          </div>
        </div>
      </div>
      <mainList :nav_main_list="nav_main_list" option-size></mainList>
    </div>
  </div>
</template>

<script>
import mainList from "../index/nav/main_list.vue";

export default {
  components: { mainList },
  data() {
    return {
      id: true,
      info: {
        balance: 100,
        smsNum: 200,
      },
      userInfo:{},
      nav_main_list: [
        {
          url: 'pages/page_lzj/mine/serviceManual/serviceManual',
          icon: "\ue62e",
          text: "使用手册",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lzj/mine/commonProblem/commonProblem',
          icon: "\ue628",
          text: "常见问题",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lzj/mine/feedback/feedback',
          icon: "\ue62f",
          text: "意见反馈",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lm/expensesExplain/expensesExplain',
          icon: "\ue638",
          text: "资费说明",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lm/smsTemplate/smsTemplate',
          icon: "\ue641",
          text: "短信模板",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lzj/mine/systemMessages/systemMessages',
          icon: "\ue640",
          text: "系统消息",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lzj/mine/setting/setting',
          icon: "\ue63b",
          text: "设置",
          color: "#0077E2",
          num: ''
        },
        {
          url: 'pages/page_lzj/mine/about/about',
          icon: "\ue62b",
          text: "关于",
          color: "#0077E2",
          num: ''
        },
        {
            url: 'pages/page_lm/InvitationCode/InvitationCode',
            icon: "\ue63e",
            text: "邀请码",
            color: "#0077E2",
            num: ''
        }
      ]
    };
  },
  async created(){
    // 获取身份
    this.id = await this.verdictId();

    // 初始更新一次
    this.getItem("userInfo",res=>{
      this.userInfo = res.data
    });

    // 进入页面时执行
    this.userInfo = await this.init();
    // 页面显示时  dataList状态触发更新
    await this.onResume(this.init, 'userInfo');
  },
  methods: {
    toPage(url){
      this.push(url)
    },
    init(){
      return new Promise(resolve => {
        this.fetch({
          url: '/orderUser/getusercode',
          body: {}
        }, result => {
          resolve(result.res.data);
        });
      });
    }
  }
};
</script>

<style scoped>
.header{
  height: 280px;
  background-color: #0077E2;
}
.userInfo{
  color: #fff;
  font-size: 36px;
}
.moneyLeft,
.smsRight{
  flex: 1;
  align-items: center;
}
.moneyLeft{
  border-right-width: 2px;
  border-right-style: solid;
  border-right-color: #E6E6E6;
}
.smsNum{
  margin-top:24px;
  margin-bottom: 14px;
  color: #999999;
  font-size: 26px;
}
.top_up_recharge{
  height:50px;
  background-color:rgba(0,119,226,1);
  color: #fff;
  font-size: 30px;
  text-align: center;
  line-height: 50px;
  border-radius:25px;
}
</style>
