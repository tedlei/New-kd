<template>
  <div class="ql-app">
    <headerNav title = '快件详情' rightButton=""></headerNav>
    <ul class="qlul bd">
        <li class="qlli">
            <text class="fontStyle">运单号</text>
        </li>
        <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.waybillNUm}}</text>
        </li>
        <template v-if="userRole==='2'">
          <li class="qlli">
              <text class="fontStyle"></text>
          </li>
          <li class="qlli qlli1">
              <text class="fontStyle">{{expressage.company}}</text>
          </li>
        </template>
        <template v-if="expressage.cargoNum">
          <li class="qlli">
              <text class="fontStyle">货号</text>
          </li>
          <li class="qlli qlli1">
              <text class="fontStyle">{{expressage.cargoNum}}</text>
          </li>
        </template>

        <li class="qlli">
            <text class="fontStyle">要送达地址</text>
        </li>
        <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.address}}</text>
        </li>
        <template v-if="expressage.agentAddress">
          <li class="qlli">
              <text class="fontStyle">错放地址</text>
          </li>
          <li class="qlli qlli1">
              <text class="fontStyle">{{expressage.agentAddress}}</text>
          </li>
        </template>
        <template v-if="expressage.uName">
          <li class="qlli">
              <text class="fontStyle">用户姓名</text>
          </li>
          <li class="qlli qlli1">
              <text class="fontStyle">{{expressage.uName}}</text>
          </li>
        </template>
        <li class="qlli">
            <text class="fontStyle">用户电话</text>
        </li>
        <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.phone}}</text>
        </li>
        <li class="qlli">
            <text class="fontStyle">入库时间</text>
        </li>
        <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.enterTime}}</text>
        </li>
        <template v-if="expressage.timeNum">
          <li class="qlli">
              <text class="fontStyle">在库时间</text>
          </li>
          <li class="qlli qlli1">
              <text class="fontStyle">{{expressage.timeNum}}小时</text>
          </li>
        </template>
        <li class="qlli">
            <text class="fontStyle">状态</text>
        </li>
        <li class="qlli qlli1">
            <text class="fontStyle" :class="[deliveryState==='7'?'':'colorRed']">{{ztDs}}</text>
        </li>
    </ul>
    <template v-if="selNum===6">
      <text v-if="deliveryState==='6'" class="notarize" @click="topQuit" :style="bd.borderWS">处理</text>
    </template>
  </div>
</template>

<script>
import bd from "../../../common/baseStyle.js";
export default {
  data() {
    return {
      bd,
      expressage: {},
      selNum: 5,
      deliveryState: "1",
      userRole: '1', //是否为快递员
    };
  },
  async created() {
    let value = await this.getParam();
    this.expressage = value.expressage;
    this.deliveryState = value.expressage.deliveryState;
    this.selNum = value.selNum * 1;
  },
  mounted(){
    this.getItem("userInfo", res => {
      if (res.result==='success') this.userRole = res.data.userRole;
    });
  },
  methods: {
    //确认退件
    topQuit: function() {
      let url = "manager/order/dispose";
      let body = { orderId: this.expressage.waybillNUm };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.deliveryState = '7';
          } else{
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
  },
  computed: {
    ztDs: function() {
      let num = this.deliveryState;
      let str = "";
      // this.toast(num)
      switch (num) {
        case "1":
          str = "未出库";
          break;
        case "2":
          str = "取件出库";
          break;
        case "3":
          str = "已添加退件列表";
          break;
        case "4":
          str = "退件出库";
          break;
        case "5":
          str = "错放件";
          break;
        case "6":
          str = "缺失件";
          break;
        case "7":
          str = "已处理";
          break;
      }
      return str;
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
  width: 200px;
}
.qlli1{
  width: 400px;
}
.fontStyle {
  font-size: 32px;
  color: rgba(102, 102, 102, 1);
}
.notarize {
  margin-left: 235px;
  margin-top: 20px;
  width: 280px;
  height: 88px;
  text-align: center;
  line-height: 88px;
  border-color: rgba(51, 51, 51, 1);
  border-radius: 5px;
}
.colorRed {
  color: red;
}
</style>
