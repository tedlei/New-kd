<template>
  <div>
    <headerNav title="邀请码" rightButton=""></headerNav>
    <scroller>
      <div class="div_row">
        <text class="div_text">50条</text>
        <div class="div_div">
          <text class="div_div_text1"></text>
          <text class="div_div_text1 d_right"></text>
          <text class="div_div_text1 d_right d_left"></text>
        </div>
          <text class="iconfont">&#xe7aa;</text>
        <div class="div_div">
          <text class="div_div_text1 d_r_right"></text>
          <text class="div_div_text1 d_r_top"></text>
          <text class="div_div_text1 d_r_right d_r_w36"></text>
        </div>
        <text class="div_text">50条</text>
      </div>
      <text class="text_font">您的邀请码：{{InvitationCode}}</text>
      <text class="text_btn" @click="copy">复制邀请码</text>
      <template v-if="isShowInput">
        <text class="text_font">输入好友的邀请码</text>
        <input type="text" placeholder="输入邀请码" v-model="inputInvCode" class="temp_input"></input>
        <text class="temp_input btn" @click="topAffirm">确认</text>
      </template>
      <div class="div_h">
        <div class="div_hx"></div>
        <text class="text_font textT">规则说明</text>
        <div class="div_hx toRight"></div>
      </div>
      <text class="text_font_size">1.每邀请一位好友，双方均可以获得50条短信</text>
      <text class="text_font_size">2.邀请好友无上限。</text>
      <text class="text_font_size">3.邀请成功，短信可在余额处查看。</text>
    </scroller>
  </div>
</template>

<script>
export default {
  data() {
    return {
      InvitationCode: "",
      inputInvCode: "",
      isShowInput: true //是否显示邀请码输入框
    };
  },

  mounted() {
    this.getItem("userInfo", res => {
      if (res.result === "success") {
        this.InvitationCode = res.data.userInvitationCode;
        this.isShowInput = res.data.userFillCode ? false : true;
      }
    });
  },
  methods: {
    copy: function() {
      let clipborad = weex.requireModule("clipboard");
      clipborad.getString(res => {
        if (res.result == "success" && this.InvitationCode === res.data) {
          this.toast("亲，你已经复制过了！！");
        } else {
          clipborad.setString(this.InvitationCode);
          this.toast("复制成功");
        }
      });
    },
    topAffirm: function() {
      let inp = this.inputInvCode.toLocaleUpperCase();
      if (!inp.trim()||inp.length<6) {
        this.toast("邀请码格式不正确！！！");
        return;
      }
      if (inp === this.InvitationCode || inp === this.InvitationCode) {
        this.toast("亲，不能输入自己的邀请码！！！");
        return;
      }
      let url = "orderUser/setusercode";
      let body = { code: inp };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.toast("您已获得50条短信，请查看短信条数！！");
            this.isShowInput = false;
            this.setItem("userInfo", obj.data1);
          } else {
            this.toast(obj.message);
          }
        },
        err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        },
        true
      );
    }
  }
};
</script>

<style scoped>
.div_row {
  margin-top: 80px;
  padding-left: 60px;
  padding-right: 60px;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.div_text {
  width: 80px;
  height: 80px;
  border-style: solid;
  border-width: 6px;
  border-color: #ffc2aa;
  border-radius: 40px;
  background-color: #ea8057;
  text-align: center;
  line-height: 68px;
  color: white;
  font-size: 26px;
}
.div_div {
  width: 72px;
}
.div_div_text1 {
  width: 60px;
  height: 8px;
  background-color: rgba(204, 204, 204, 1);
  border-radius: 4px;
}
.d_right {
  margin-top: 14px;
  margin-left: 24px;
  width: 48px;
}
.d_left {
  width: 36px;
  margin-left: 0;
}
.d_r_right {
  margin-left: 12px;
}
.d_r_top {
  margin-top: 14px;
  width: 48px;
}
.d_r_w36 {
  margin-top: 14px;
  margin-left: 36px;
  width: 36px;
}
.iconfont {
  font-family: iconfont;
  /* background-image: linear-gradient(to top, #0077e2, #adddfe);; */
  color: #0077e2;
  font-size: 108px;
}
.text_font {
  margin-top: 80px;
  text-align: center;
  font-size: 36px;
  font-weight: bold;
  color: rgba(51, 51, 51, 1);
}
.text_btn {
  margin-top: 40px;
  margin-left: 208px;
  width: 334px;
  height: 88px;
  background-color: rgba(0, 119, 226, 1);
  border-radius: 10px;
  text-align: center;
  line-height: 88px;
  font-size: 36px;
  color: white;
}
.temp_input {
  margin-top: 40px;
  margin-left: 30px;
  width: 690px;
  height: 88px;
  border-style: solid;
  border-color: #0077e2;
  border-width: 2px;
  border-radius: 10px;
  text-align: center;
  font-size: 36px;
  color: rgba(204, 204, 204, 1);
}
.btn {
  margin-top: 60px;
  background-color: #0077e2;
  line-height: 84px;
  color: white;
}

.div_h {
  height: 36px;
  margin-top: 80px;
  padding-left: 128px;
  padding-right: 128px;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.div_hx {
  width: 164px;
  height: 4px;
  background-image: linear-gradient(to right, #fff, #000);
}
.toRight {
  background-image: linear-gradient(to left, #fff, #000);
}
.textT {
  margin-top: 0;
}
.text_font_size {
  margin-top: 20px;
  margin-left: 32px;
  font-size: 28px;
  color: rgba(51, 51, 51, 1);
}
</style>
