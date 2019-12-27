<template>
  <div class="ql-app">
    <headerNav title = '快件详情' rightButton=""></headerNav>
    <ul class="qlul bd">
        <li class="qlli">
            <text class="fontStyle">运单号</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">{{expressage.waybillNUm}}</text>
        </li>
        <template v-if="userRole==='2'">
        <li class="qlli">
            <text class="fontStyle">快递公司</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">{{expressage.company}}</text>
        </li>
        </template>
        <template v-if="expressage.cargoNum">
          <li class="qlli">
              <text class="fontStyle">货号</text>
          </li>
          <li class="qlli">
              <text class="fontStyle">{{expressage.cargoNum}}</text>
          </li>
        </template>

        <template v-if="expressage.courierName&&userRole==='2'">
          <li class="qlli">
              <text class="fontStyle">快递员姓名</text>
          </li>
          <li class="qlli">
              <text class="fontStyle">{{expressage.courierName}}</text>
          </li>
        </template>
        <template v-if="expressage.courierPhone&&userRole==='2'">
          <li class="qlli">
              <text class="fontStyle">快递员电话</text>
          </li>
          <li class="qlli">
              <text class="fontStyle">{{expressage.courierPhone}}</text>
          </li>
        </template>
        <template v-if="expressage.courierLocaltion">
          <li class="qlli">
              <text class="fontStyle">代办点地址</text>
          </li>
          <li class="qlli">
              <text class="fontStyle">{{expressage.courierLocaltion}}</text>
          </li>
        </template>
        <li class="qlli">
            <text class="fontStyle">用户姓名</text>
        </li>
        <li class="qlli">
          <text class="fontStyle">{{expressage.uName?expressage.uName:'未填写'}}</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">用户电话</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">{{expressage.phone}}</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">入库时间</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">{{expressage.enterTime}}</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">在库时间</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">{{expressage.timeNum}}小时</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">是否通知</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">{{expressage.inform==='1'?'未通知':'已通知'}}</text>
        </li>
    </ul>
    <div class="inform">
      <text class="inftext" v-if="expressage.inform==='1'&&startNum===1" :style="bd.borderWS" @click="topMsgInfo(expressage.waybillNUm,expressage.phone,expressage.cargoNum)">短信通知客户</text>
      <text class="inftext" v-if="expressage.inform==='2'&&startNum===1" :style="bd.borderWS" >已通知客户</text>
      <text class="inftext" v-if="expressage.inform==='2'&&startNum===2&&expressage.deliveryState==='1'&&userRole==='2'" :style="bd.borderWS" @click="quitjian(expressage.waybillNUm,expressage.courierPhone)">添加退件列表</text>
      <text class="inftext" v-if="expressage.deliveryState==='3'&&startNum===2" :style="bd.borderWS">已添加至退件列表</text>
    </div>
  </div>
</template>

<script>
import bd from "../../../common/baseStyle.js";
export default {
  data() {
    return {
      bd,
      startNum: 1,
      userRole: '1',
      expressage: {},
      userOrderSms:'',  
      smsTemplate:'',  
    };
  },

  async created() {
    let value = await this.getParam();
    this.expressage = value.expressage;
    this.startNum = value.startNum * 1;
  },
  mounted() {
    this.getItem("userInfo",res=>{
      this.userOrderSms= res.data.userOrderSms;
      this.userRole = res.data.userRole;

      this.getItem('smsList',res=>{
        let obj = res.data[1];
        for(let o of obj){
          if(o.id===this.userOrderSms) this.smsTemplate = o.smsTemplate
        }
      });
    });
  },
  methods: {
    //点击短信通知
    topMsgInfo: function(orderId, phone, shelves) {
      let url = "manager/order/smsinform";
      let body = { orderId, phone, shelves,content:this.smsTemplate};
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.expressage.inform = "2";
          } else if (obj.code === 401) {
            this.toast(obj.message);
          }
        },
        err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        }
      );
    },

    //点击退件处理
    quitjian: function(orderId, userId) {
      let hint = "";
      let url = "manager/order/orderbounce";
      let body = { orderId, userId };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (obj.message === "1")
              hint = "已通知快递员,并成功添加到退件列表,请到退件处理进行退件";
            else if (obj.message === "2")
              hint = "成功添加到退件列表,请到退件处理进行退件";
            // this.orderStatus = "3";
            this.expressage.deliveryState = "3";
            this.toast(hint);
          } else if (obj.code === 401) {
            this.toast(obj.message);
          }
        },
        err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        }
      );
    }
  },
  computed:{
    param:function(){
      return {num:this.expressage.inform}
    }
  }
};
</script>

<style scoped>
.bd {
  border-width: 1px;
  border-style: solid;
}
.qlul {
  margin-left: 30px;
  margin-top: 25px;
  padding-bottom: 30px;
  width: 690px;
  border-color: rgba(230, 230, 230, 1);
  border-radius: 10px;
  flex-direction: row;
  flex-wrap: wrap;
}
.qlli {
  margin-top: 30px;
  margin-left: 24px;
  width: 320px;
}
.fontStyle {
  font-size: 32px;
  color: rgba(102, 102, 102, 1);
}
.qlula {
  margin-left: 30px;
  margin-top: 32px;
  width: 690px;
  height: 80px;
  border-color: rgba(230, 230, 230, 1);
  border-top-width: 1px;
  border-bottom-width: 1px;
  border-style: solid;
  flex-direction: row;
}
.qllia {
  width: 230px;
  height: 80px;
}
.textal {
  text-align: center;
  line-height: 80px;
  color: rgba(102, 102, 102, 1);
}
.leftBd {
  border-left-color: rgba(230, 230, 230, 1);
  border-left-style: solid;
  border-left-width: 1px;
}
.btnDiv {
  margin-top: 30px;
  margin-left: 262px;
  width: 230px;
  height: 80px;
  background-color: rgba(204, 204, 204, 1);
}
.cen {
  text-align: center;
  line-height: 80px;
  color: rgba(51, 51, 51, 1);
}
.inform {
  margin-top: 20px;
  height: 88px;
  flex-direction: row;
  justify-content: center;
}
.inftext {
  width: 300px;
  height: 88px;
  line-height: 88px;
  text-align: center;
  border-color: rgba(102, 102, 102, 1);
  border-radius: 10px;
  font-size: 28px;
  color: rgba(51, 51, 51, 1);
}
.wid {
  width: 200px;
  margin-left: 60px;
  margin-right: 60px;
}
</style>
