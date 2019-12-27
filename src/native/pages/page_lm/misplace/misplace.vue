<template>
  <div class="misplace-app">
    <headerNav title='异常件' rightButton=""></headerNav>
    <div class="misDiv">
      <text class="misFc" :class="[selNum===5?'fontStyle':'']" @click="topSel(5)">错放件</text>
      <text class="misFc" :class="[selNum===6?'fontStyle':'']" @click="topSel(6)">缺失件</text>
    </div>
    <scroller class="misScr" v-if="list.length>0">
      <misList v-for="(e,i) of list" :key="i" :obj="e" :selNum="selNum" @getInformList="getList"></misList>
    </scroller>
    <text class="text_font" v-else>没有数据！！</text>
  </div>
</template>

<script>
import bd from "../../../common/baseStyle.js";
import misList from "./misList.vue";
export default {
  beforeCreate() {
    weexModule.call(this);
  },
  components: { misList },
  data() {
    return {
      bd,
      selNum: 5,
      list: []
    };
  },
  created() {
    this.getList(this.selNum);
  },
  methods: {
    //切换时
    topSel: function(num) {
      this.selNum = num;
      this.getList(num);
    },

    //获取数据
    getList: function(num) {
      this.list = [];
      let url = "manager/order/getanomaly";
      let body = { orderstatus: num };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (obj.data.length > 0) {
              this.circulation(obj.data);
            } else {
              this.toast("暂未查询到数据！！！");
            }
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

    // 获取对象的值
    circulation: function(obj) {
      let arr = [];
      for (let o in obj) {
        let om = obj[o].orderManager;
        let dx = {};
        dx.address = obj[o].address;
        dx.agentAddress = om.agentAddress;
        dx.company = om.company;
        dx.waybillNUm = om.orderId;
        dx.cargoNum = om.shelves;
        dx.deliveryState = om.orderStatus;
        dx.phone = om.expressOwnerTel;
        dx.uName = om.nickName;
        dx.enterTime = om.handleTime;
        dx.endTime = om.ckTime;
        dx.timeNum = om.day;
        dx.inform = om.smsStatus;
        dx.courierName = om.userName;
        dx.courierPhone = om.userId;
        arr.push(dx);
      }
      this.list = arr;
    }
  }
};
</script>

<style scoped>
.misplace-app {
  flex: 1;
}
.misDiv {
  height: 88px;
  flex-direction: row;
  border-bottom-style: solid;
  border-bottom-width: 2px;
  background-color: rgba(204, 204, 204, 1);
  border-bottom-color: rgba(242, 242, 242, 1);
}
.misFc {
  width: 375px;
  height: 86px;
  text-align: center;
  line-height: 86px;
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.fontStyle {
  background-color: white;
  color: rgba(0, 119, 226, 1);
}
.misScr {
  flex: 1;
}
.text_font{
  margin-top: 40px;
  text-align: center;
  font-size: 30px;
}
</style>
