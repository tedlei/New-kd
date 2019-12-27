<template>
  <div class="ql-app">
    <headerNav title = '快件详情' rightButton=""></headerNav>
    <ul class="qlul bd">
      <li class="qlli">
        <text class="fontStyle">运单号</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle">{{expressage.orderId}}</text>
      </li>
      <template v-if="userRole==='2'">
        <li class="qlli">
          <text class="fontStyle">快递公司</text>
        </li>
        <li class="qlli qlli1">
          <text class="fontStyle">{{expressage.company}}</text>
        </li>
      </template>
      <template v-if="expressage.shelves">
        <li class="qlli">
          <text class="fontStyle">货号</text>
        </li>
        <li class="qlli qlli1">
          <text class="fontStyle">{{expressage.shelves}}</text>
        </li>
      </template>
      <template v-if="userId===2">
        <template v-if="expressage.userName">
          <li class="qlli">
            <text class="fontStyle">快递员姓名</text>
          </li>
          <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.userName}}</text>
          </li>
        </template>
        <template v-if="expressage.userId">
          <li class="qlli">
            <text class="fontStyle">快递员电话</text>
          </li>
          <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.userId}}</text>
          </li>
        </template>
      </template>

      <template v-if="userRole==='1'&&expressage.deliveryPhone">
        <li class="qlli">
          <text class="fontStyle">要送达地址</text>
        </li>
        <li class="qlli qlli1">
          <text class="fontStyle">{{expressage.deliveryPhone}}</text>
        </li>
        <template v-if="expressage.agentAddress&&expressage.orderStatus!=='7'">
          <li class="qlli">
          <text class="fontStyle">已送达地址</text>
          </li>
          <li class="qlli qlli1">
            <text class="fontStyle">{{expressage.agentAddress}}</text>
          </li>
        </template>
      </template>
      <li class="qlli">
        <text class="fontStyle">用户姓名</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle">{{expressage.nickName?expressage.nickName:'未填写'}}</text>
      </li>
      <li class="qlli">
        <text class="fontStyle">用户电话</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle">{{expressage.expressOwnerTel}}</text>
      </li>
      <li class="qlli">
        <text class="fontStyle">入库时间</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle">{{expressage.handleTime}}</text>
      </li>
      <li class="qlli">
        <text class="fontStyle">在库时间</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle">{{expressage.day}}小时</text>
      </li>
      <template v-if="expressage.ckTime">
        <li class="qlli">
          <text class="fontStyle">出库时间</text>
        </li>
        <li class="qlli qlli1">
          <text class="fontStyle">{{expressage.ckTime}}</text>
        </li>
      </template>
      <li class="qlli">
        <text class="fontStyle">出库状态</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle" :class="[expressage.orderStatus==='5'||expressage.orderStatus==='6'?'colorRed':'']">{{ztDs}}</text>
      </li>
      <li class="qlli">
        <text class="fontStyle">是否通知</text>
      </li>
      <li class="qlli qlli1">
        <text class="fontStyle">{{expressage.smsStatus==='1'?'未通知':'已通知'}}</text>
      </li>
      <li class="qlli qlli1" v-if="expressage.orderRemark">
        <text class="fontStyle">备注:{{expressage.orderRemark}}</text>
      </li>
    </ul>
    <div v-if="userRole==='2'&&boole">
      <text v-if="expressage.orderStatus==='1'||expressage.orderStatus==='3'" @click='topQuit' :style="bd.borderWS" class="notarize">确认退件</text>
    </div>
  </div>
</template>

<script>
import bd from "../../../common/baseStyle.js";
export default {
  data() {
    return {
      bd,
      expressage: {},
      boole: false,
      userRole: "1" //是否为快递员
    };
  },
  async created() {
    let value = await this.getParam();
    if (value.expressage) {
      this.expressage = value.expressage;
      this.boole = value.boole;
    } else this.getWaybillNUm(value.orderNum);
  },
  mounted() {
    this.getItem("userInfo", res => {
      if (res.result === "success") this.userRole = res.data.userRole;
    });
  },
  methods: {
    getWaybillNUm: function(orderId) {
      let url = "manager/order/find";
      let body = { orderId };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (obj.data.length > 0) this.expressage = obj.data[0];
            else this.toast("未查询到该快件信息！！");
          } else {
            this.toast(obj.message);
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！！！");
        },
        true
      );
    },

    //确认退件
    topQuit: function() {
      let url = "manager/order/determine";
      let body = { orderId: this.expressage.orderId };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.expressage.orderStatus = "4";
            this.expressage.endTime = obj.data1;
            this.audio("handBackSucceed", "handBackHint");
          } else {
            this.toast(obj.message);
            this.audio("handBackFailed", "handBackHint");
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！！！");
          this.audio("handBackFailed", "handBackHint");
        },
        true
      );
    }
  },
  computed: {
    ztDs: function() {
      let shelves = this.expressage.shelves;
      let num = this.expressage.orderStatus + "";
      let str = "";
      switch (num) {
        case "1":
          if (shelves) str = "未出库";
          else str = "代办点未入库";
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
          str = "已放置";
          break;
      }
      return str;
    },
    isShowbtn: function() {
      let num = this.expressage.orderStatus + "";
      let userRole = this.userRole;
      let boole = false;
      if (userRole === "2" && (num === "1" || num === "3")) {
        boole = true;
      }
      return boole;
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
.qlli1 {
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