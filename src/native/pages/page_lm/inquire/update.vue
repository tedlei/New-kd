<template>
  <div class="query-app">
    <headerNav title = '查询' rightButton=""></headerNav>
    <ul class="qaul">
        <li class="qali">
            <text class="qaFont">运单号</text>
            <input type="text" class="qaInp" disabled=true :value="expressage.orderId" placeholder="请输入运单号"></input>
        </li>
    </ul>
    <ul class="qaul">
        <li class="qali">
            <text class="qaFont">姓名</text>
            <input type="text" class="qaInp bd" :class="[tNameCorrect?'borCol':'']" v-model="getName" placeholder="请输入姓名"></input>
        </li>
    </ul>
    <ul class="qaul">
        <li class="qali">
            <text class="qaFont">电话</text>
            <input type="text" class="qaInp bd" :class="[phoneCorrect?'':'borCol']" v-model="getPhonr" placeholder="请输入电话"></input>
        </li>
    </ul>
    <text class="text_font">温馨提示:</text>
    <text class="text_font">如果修改电话号码会重发短信通知，未修改则不会重发短信</text>
    <text class="btnSty" @click="topSend">修改</text>
  </div>
</template>

<script>
import infoVerify from "../../../common/infoVerify.js";
export default {
  data() {
    return {
      expressage: {},

      getName: "", //获取姓名
      getPhonr: "", //获取电话

      phoneCorrect: false, //电话验证
      tNameCorrect: true //验证姓名
    };
  },
  async created() {
    let value = await this.getParam();
    this.expressage = value.expressage;
    this.getName = value.expressage.nickName;
    this.getPhonr =value.expressage.expressOwnerTel;
  },

  methods: {
    //点击重新发送
    topSend: function() {
      if (!this.getName || this.tNameCorrect) {
        this.toast("姓名为空或格式错误！！！");
        return;
      }
      if (!this.getPhonr || !this.phoneCorrect) {
        this.toast("手机号码为空或格式错误！！！");
        return;
      }
      if (
        this.getName === this.expressage.nickName &&
        this.getPhonr === this.expressage.expressOwnerTel
      ) {
        this.toast("未修改姓名和电话！！");
        return;
      }
      let body;
      if (this.getPhonr !== this.expressage.expressOwnerTel) {
        body = {
          orderId: this.expressage.orderId,
          nickname: this.getName,
          phone: this.getPhonr
        };
      } else {
        body = { orderId: this.expressage.orderId, nickname: this.getName };
      }
      let url = "manager/order/updateorderid";
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.toast(obj.message);
            this.expressage.phone = this.getPhonr;
            this.expressage.uName = this.getName;
            this.pop();
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
  watch: {
    getName: function(val) {
      infoVerify.username.call(this, val);
      if(val.length===1)this.tNameCorrect=false;
    },
    getPhonr: function(val) {
      infoVerify.phone.call(this, val);
    }
  }
};
</script>

<style scoped>
.bd {
  border-width: 1px;
  border-style: solid;
}
.query-app {
  width: 750px;
}
.qaul {
  margin-left: 74px;
}
.qali {
  margin-top: 20px;
  width: 750px;
  flex-direction: row;
  justify-content: flex-start;
}
.qaFont {
  width: 150px;
  height: 88px;
  line-height: 88px;
  font-size: 32px;
  color: rgba(102, 102, 102, 1);
}
.qaInp {
  width: 400px;
  height: 88px;
  padding-left: 30px;
  font-size: 32px;
  border-color: rgba(204, 204, 204, 1);
  color: rgba(104, 104, 104, 1);
}
.btnSty {
  margin-top: 40px;
  margin-left: 260px;
  width: 230px;
  height: 80px;
  text-align: center;
  line-height: 80px;
  background-color: rgba(0, 119, 226, 1);
  border-radius: 10px;
  font-size: 32px;
  color: rgba(254, 254, 254, 1);
}
.borCol {
  border-color: red;
}
.text_font{
  margin-top: 20px;
  margin-left: 80px;
}
</style>
