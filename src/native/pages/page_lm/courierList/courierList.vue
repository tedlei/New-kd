<template>
  <div class="ql-app">
    <div class="qlDiv1 bd" @click="topList">
      <div class="qldivList">
        <ul class="qlul">
          <li class="qlli">
            <text class="fontStyle">{{expressage.company}}</text>
          </li>
          <li class="qlli1">
            <text class="fontStyle">{{expressage.orderId}}</text>
          </li>
        </ul>
        <ul class="qlul">
          <li class="qlli">
            <text class="fontStyle">状态</text>
          </li>
          <li class="qlli1">
            <text class="fontStyle" :class="[expressage.orderStatus==='5'||expressage.orderStatus==='6'?'colorRed':'']">{{ztDs}}</text>
          </li>
        </ul>
        <ul class="qlul">
          <li class="qlli">
            <text class="fontStyle">用户电话</text>
          </li>
          <li class="qlli1">
            <text class="fontStyle">{{expressage.expressOwnerTel}}</text>
          </li>
        </ul>
        <ul class="qlul">
          <li class="qlli">
            <text class="fontStyle">姓名</text>
          </li>
          <li class="qlli1">
            <text class="fontStyle">{{expressage.nickName?expressage.nickName:'未填写'}}</text>
          </li>
        </ul>
      </div>
      <div class="qlIcon">
          <wxc-icon name="more" @wxcIconClicked="topList"></wxc-icon>
      </div>
    </div>
    <div v-if="userRole===2&&boole===undefined">
      <text v-if="expressage.orderStatus==='1'||expressage.orderStatus==='3'" @click='topQuit' :style="bd.borderWS" class="notarize">确认退件</text>
    </div>

    <div v-else>
      <text v-if="expressage.orderStatus==='1'||expressage.orderStatus==='3'" :style="bd.borderWS" @click="dxcf" class="notarize">短信重发</text>
    </div>
  </div>
</template>

<script>
import { WxcIcon } from "weex-ui";
import bd from "../../../common/baseStyle.js";
export default {
  components: { WxcIcon },
  props: ["obj", "userRole","boole","repertory"],
  data() {
    return {
      bd,
      expressage: {},
    };
  }, 
  mounted() {
    this.expressage = this.obj;
  },
  methods: {
    //点击列表时
    topList: function() {
        this.navigator.pushFull(
        {
            url: "root:pages/page_lm/courierDetail/courierDetail.js",
            param: { expressage:this.expressage,boole:this.boole}
        },
        () => {this.$emit("topQuery")}
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
                    this.$emit("topQuery");
                    // this.expressage.orderStatus = '4';
                    // this.expressage.endTime = obj.data1;
                    this.audio("handBackSucceed", "handBackHint");
                } else{
                    this.toast(obj.message);
                    this.audio("handBackFailed", "handBackHint");
                }
            },
            err => {
                this.toast("网络错误,请检查您的网络！！！");
                this.audio("handBackFailed", "handBackHint");
            },true
        );
    },
    //短信重发
    dxcf:function(){
      let url = "/manager/order/masssms";
      let body = { number:"2",sms:[this.expressage]};
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.toast('短信重发成功！')
          } else{
            this.toast('短信重发失败！')
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！");
        },true
      );
    },
  },
  computed: {
    ztDs: function() {
      let num = this.expressage.orderStatus;
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
  height: 266px;
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
  width: 170px;
}
.qlli1 {
  width: 390px;
}
.fontStyle {
  font-size: 32px;
  color: rgba(102, 102, 102, 1);
}
.qlIcon {
  height: 266px;
  padding-top: 100px;
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
.colorRed{
  color:red;
}
</style>
