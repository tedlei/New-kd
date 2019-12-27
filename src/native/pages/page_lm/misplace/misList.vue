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
                  <text class="fontStyle">姓名</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle">{{expressage.uName?expressage.uName:'无'}}</text>
              </li>
          </ul>



          <ul class="qlul">
            <li class="qlli">
              <text class="fontStyle">要送达地址</text>
            </li>
            <li class="qlli qlli1">
              <text class="fontStyle">{{expressage.address}}</text>
            </li>
          </ul>



          <ul class="qlul">
              <li class="qlli">
                  <text class="fontStyle">状态</text>
              </li>
              <li class="qlli1">
                  <text class="fontStyle" :class="[deliveryState==='7'?'':'colorRed']">{{ztDs}}</text>
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
      </div>
      <div class="qlIcon">
          <wxc-icon name="more" @wxcIconClicked="topList"></wxc-icon>
      </div>
    </div>
    <template>
      <text class="qlcl" @click="topcl" :style="bd.borderWS">处理</text>
    </template>
  </div>
</template>

<script>
import { WxcIcon } from "weex-ui";
import bd from "../../../common/baseStyle.js";
export default {
  components: { WxcIcon },
  props: ["obj", "selNum"],
  data() {
    return {
      bd,
      expressage: {},
      deliveryState: "1"
    };
  },
  mounted() {
    this.expressage = this.obj;
    this.deliveryState = this.obj.deliveryState;
  },
  methods: {
    //点击时
    topList: function() {
      this.navigator.pushFull(
        {
          url: "root:pages/page_lm/misplace/misDetail.js",
          param: {
            expressage: this.expressage,
            selNum: this.selNum
          }
        },
        () => {
          if(this.selNum===5) return
          this.$emit("getInformList",this.selNum);
        }
      );
    },

    topcl: function() {
      let url = "manager/order/dispose";
      let body = { orderId: this.expressage.waybillNUm };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.deliveryState = "7";
            this.expressage.deliveryState = "7";
            this.$emit("getInformList",this.selNum);
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
  computed: {
    ztDs: function() {
      let num = this.deliveryState;
      let str = "";
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
.qlDiv1 {
  margin-left: 30px;
  width: 690px;
  height: 326px;
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
  height: 326px;
  padding-top: 130px;
}
.qlcl {
  margin-top: 20px;
  margin-left: 285px;
  width: 180px;
  height: 88px;
  text-align: center;
  line-height: 88px;
  background-color: rgba(255, 255, 255, 1);
  border-color: rgba(102, 102, 102, 1);
  border-radius: 10px;
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.colorRed {
  color: red;
}
</style>
