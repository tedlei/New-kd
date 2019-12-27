<template>
  <div class="st-app">
    <headerNav title="短信模板" rightButton></headerNav>
    <div class="stSel">
      <text class="stSelFont" @click="topSel(1)" :class="[selNum===1?'bgColor':'']">取件通知</text>
      <text class="stSelFont" @click="topSel(2)" :class="[selNum===2?'bgColor':'']">滞留件通知</text>
    </div>
    <div class="stFontDiv" @click="topNewTeam">
      <text class="stFont1">+</text>
      <text class="stFont2">新增模板</text>
    </div>
    <scroller class="stSmsList">
      <div class="stList" v-for="(e,i) of smsList[selNum-1]" :key="i" @click="topTemplate(selNum,i)">
        <div class="stDivflex">
          <text class="dFont">默认模板</text>
          <div v-if="selNum===1" class="slIcon" :class="[informNum===i?'bgColor1':'']" :style="bd.borderWS">
            <text :class="[informNum===i?'fontColor':'']" style="font-family:iconfont;">&#xe614;</text>
          </div>
          <div v-if="selNum===2" class="slIcon" :class="[retentionNum===i?'bgColor1':'']" :style="bd.borderWS">
            <text :class="[retentionNum===i?'fontColor':'']" style="font-family:iconfont;">&#xe614;</text>
          </div>
        </div>
        <text class="stListFont">{{e.smsTemplate}}</text>
      </div>
    </scroller>
  </div>
</template>

<script>
import bd from '../../../common/baseStyle.js'
export default {
  data() {
    return {
      bd,
      selNum: 1,
      informNum:0, //短信通知
      retentionNum:0, //滞留件
      smsList: [],
      userSms:'',
      userOrderSms:'',
      userInfo:{},
    };
  },
  created(){
    this.getItem("userInfo",res=>{
      this.userInfo = res.data;
      this.userSms = res.data.userSms;
      this.userOrderSms = res.data.userOrderSms;
      this.getItem('smsList',res=>{
        this.smsList = res.data;
        for(let o in this.smsList[0]){
          if(this.smsList[0][o].id===this.userSms){
            this.informNum = o*1;
          }
        }
        for(let o in this.smsList[1]){
          if(this.smsList[1][o].id===this.userOrderSms){
            this.retentionNum = o*1;
          }
        }
      });
    })
  },
  methods: {
    topSel: function(num) {
      this.selNum = num;
    },
    
    //选择模板时
    topTemplate:function(selNum,i){
      let obj = this.smsList;
      switch(selNum){
        case 1:this.informNum = i;
          this.bb(selNum,i)
          this.userSms=obj[0][i].id
        break;
        case 2:this.retentionNum = i;
          this.userOrderSms==obj[1][i].id
          this.bb(selNum,i)
        break;
      }
    },

    topNewTeam:function(){
      this.toast('该功能暂未开通，尽请期待！！')
      return;
      this.navigator.pushFull({url:"root:pages/page_lm/smsTemplate/newStp.js",param:{selNum:this.selNum}},()=>{
        this.getItem('smsList',res=>{
          this.smsList = res.data
        });
      });
    },


    //选择短信模板
    bb:function(selNum,smsNum){
      let url = 'orderSmstemplate/setOrderSms';
      let body = {smsNum:this.smsList[0][smsNum].id,selNum:selNum+''} ;
      this.fetch(
        { url, body },
        res=>{
           let obj = res.res;
          if (obj.code=== 200) {
            let obj = this.smsList;
            let userInfo = this.userInfo;
            obj.userSms = this.smsList[selNum-1][smsNum].id;
            this.setItem("smsList",obj)

            if(selNum===1) userInfo.userSms = this.userSms;
            else userInfo.userOrderSms = this.userOrderSms;
            this.setItem("userInfo",userInfo)
            
          }
        },
        err => {
            this.toast("网络错误,请检查您的网络！！！");
        },true
      );
    }

  }
};
</script>

<style scoped>
.st-app {
  width: 750px;
  flex: 1;
}
.stSel {
  width: 750px;
  height: 88px;
  background-color: rgba(204, 204, 204, 1);
  flex-direction: row;
}
.stSelFont {
  width: 375px;
  height: 86px;
  text-align: center;
  line-height: 86px;
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.bgColor {
  background-color: white;
  color: rgba(0, 119, 226, 1);
}
.stFontDiv {
  width: 750px;
  height: 240px;
  justify-content: center;
  border-bottom-style: solid;
  border-bottom-width: 2px;
  border-bottom-color: rgba(204, 204, 204, 1);
}
.stFont1,
.stFont2 {
  text-align: center;
  font-size: 144px;
  font-weight: bold;
  color: rgba(0, 119, 226, 1);
}
.stFont2 {
  color: rgba(0, 119, 226, 1);
  font-size: 28px;
  font-weight: 500;
}
.stSmsList {
  flex: 1;
}
.stList {
  width: 750px;
  height: 270px;
  padding-left: 30px;
  padding-right: 30px;
  border-bottom-style: solid;
  border-bottom-width: 2px;
  border-bottom-color: rgba(204, 204, 204, 1);
}
.stListFont {
  margin-top: 40px;
  font-size: 28px;
  color: rgba(102, 102, 102, 1);
}
.stDivflex{
  margin-top: 40px;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.dFont{
  font-weight:bold;
  font-size: 28px;
  color: rgba(102, 102, 102, 1);
}
.slIcon{
  width: 40px;
  height: 40px;
  border-color: rgba(153,153,153,1);
  border-radius:50%;
  justify-content: center;
  align-items: center;
}
.bgColor1{
  background-color:rgba(0,119,226,1);
  border-color: rgba(0,119,226,1);
}
.fontColor{
  color:white;
}
</style>
