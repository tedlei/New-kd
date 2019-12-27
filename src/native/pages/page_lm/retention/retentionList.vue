<template>
  <div class="ql-app">
    <div class="qlDiv1 bd" @click="topList">
      <div class="qldivList">
          <ul class="qlul">
              <li class="qlli">
                  <text class="fontStyle">{{expressage.company}}</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle">{{expressage.waybillNUm}}</text>
              </li>
          </ul>
          <ul class="qlul">
              <li class="qlli">
                  <text class="fontStyle">货号</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle">{{expressage.cargoNum}}</text>
              </li>
          </ul>
          <ul class="qlul">
              <li class="qlli">
                  <text class="fontStyle">在库时间</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle">{{expressage.timeNum}}小时</text>
              </li>
          </ul>
          <ul class="qlul">
              <li class="qlli">
                  <text class="fontStyle">电话</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle">{{expressage.phone}}</text>
              </li>
          </ul>
          <ul class="qlul">
              <li class="qlli">
                  <text class="fontStyle">用户姓名</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle">{{expressage.uName?expressage.uName:'未填写'}}</text>
              </li>
          </ul>
      </div>
      <div class="qlIcon">
          <wxc-icon name="more" @wxcIconClicked="topList"></wxc-icon>
      </div>
    </div>
    <div class="inform">
      <text class="inftext" v-if="inform==='1'&&startNum==='1'" :style="bd.borderWS" @click="topMsgInfo(expressage.waybillNUm,expressage.phone,expressage.cargoNum)">短信通知客户</text>
      <text class="inftext" v-if="inform==='2'&&startNum==='1'" :style="bd.borderWS" >已通知客户</text>
      <text class="inftext" v-if="inform==='2'&&startNum==='2'&&orderStatus==='1'&&userRole=='2'" :style="bd.borderWS" @click="quitjian(expressage.waybillNUm,expressage.courierPhone)">添加退件列表</text>
      <text class="inftext" v-if="orderStatus==='3'&&startNum==='2'" :style="bd.borderWS">已添加至退件列表</text>
    </div>
  </div>
</template>

<script>
import { WxcIcon } from "weex-ui";
import bd from "../../../common/baseStyle.js";
export default {
  props: ["eobj", "startNum"],
  components: { WxcIcon },
  data() {
    return {
      bd,
      userRole: 1, //是否为代办点
      expressage: {},
      inform: "1", //客户通知
      orderStatus: "1", //库存状态
      smsTemplate:'',  

    };
  },
  created() {
    this.getItem("userInfo",res=>{
      let userOrderSms= res.data.userOrderSms;
      this.userRole = res.data.userRole*1;
      this.getItem('smsList',res=>{
        let obj = res.data[1];
        for(let o of obj){
          if(o.id===userOrderSms) this.smsTemplate = o.smsTemplate
        }
      });
    });
    this.expressage = this.eobj;
    this.inform = this.eobj.inform;
    this.orderStatus = this.eobj.deliveryState;
  },
  methods: {
    //点击短信通知
    topMsgInfo: function(orderId, phone, shelves) {
      let url = "manager/order/smsinform";
      let body = { orderId, phone, shelves ,content:this.smsTemplate};
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.inform = "2";
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

    //点击列表时
    topList: function() {
      this.navigator.pushFull(
        {
          url:'root:pages/page_lm/retention/retentionDetail.js',
          param:{expressage: this.expressage,startNum: this.startNum}
        },
        res=>{
          this.$emit("getInformList",this.startNum,0)
        }
      )
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
            this.orderStatus = "3";
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
  }
};
</script>

<style scoped>
.bd {
  border-width: 1px;
  border-style: solid;
}
.qlDiv1 {
  margin-left: 30px;
  width: 690px;
  height: 336px;
  margin-top: 32px;
  padding-left: 24px;
  padding-right: 24px;
  border-color: rgba(230, 230, 230, 1);
  border-radius: 5px;
  flex-direction: row;
  justify-content: space-between;
}
.qldivList {
  width: 550px;
}
.qlul {
  margin-top: 25px;
  flex-direction: row;
}
.qlli {
  width: 200px;
}
.qlli1 {
  width: 330px;
}
.fontStyle {
  font-size: 32px;
  color: rgba(102, 102, 102, 1);
}
.qlIcon {
  height: 336px;
  padding-top: 140px;
}
.qlula {
  margin-left: 30px;
  width: 690px;
  height: 80px;
  margin-top: 32px;
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
/* .wid{
  width: 200px;
  margin-left: 60px;
  margin-right: 60px;
} */
</style>
