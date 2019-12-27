<template>
  <div class="cs-app">
    <headerNav title="忘记密码" rightButton></headerNav>
    <scroller class="scro">
      <text class="csFont">忘记密码</text>
      <input type="number" class="inp" :autofocus="true" :class="[phoneCorrect?'':'borCol']" v-model="phone" placeholder="请输入手机号"></input>
      <div class="inpDiv">
        <input type="number" class="inpyz" :class="[smsCodeCorrect?'':'borCol']" v-model="uCode" placeholder="请输入验证码"></input>
        <wxc-button :text="isBtnDis?authTime+'秒':'获取验证码'" type='white' :btnStyle="btnStyle" @wxcButtonClicked="btnClick" :disabled="isBtnDis"></wxc-button>
      </div>

      <input type="password" class="inp" :class="[passwordCorrect?'':'borCol']" v-model="uPwd" placeholder="请输入您的密码"></input>
      <input type="password" class="inp" :upriseOffsetv="20" :class="[affirmPwdCorrect?'':'borCol']" v-model="affirmPwd" placeholder="请输入您的确认密码"></input>
      
      <wxc-button text="确定" @wxcButtonClicked='enroll' class="inp" :btnStyle="btnStyle"></wxc-button>
    </scroller>
  </div>
</template>

<script>
import { WxcButton } from "weex-ui";
import infoVerify from "../../../common/infoVerify.js";
export default {
  components: { WxcButton,},
  props: ["selNum"],
  data() {
    return {
      phone: "", //电话
      uCode: "", //验证码
      uPwd: "", //密码
      affirmPwd:'',   //确认密码

      btnStyle: {//页面按钮样式
        width: "200px",
        height: "100px",
        backgroundColor: "rgba(0,119,226,1)",
        borderRadius: "3px",
        color: "rgba(255,255,255,1)"
      },
      authTime: 60, //获取验证码倒计时
      isBtnDis: false, //禁用按钮

      phoneCorrect: true, //验证电话
      smsCodeCorrect: true, //验证验证码
      passwordCorrect: true, //验证密码
      affirmPwdCorrect: true //验证确认密码
    };
  },
  methods: {
    /**
     * 点击确定
     */
    enroll: function() {
      if (!this.phoneCorrect || !this.phone.trim()) {
        this.toast("手机号码为空或格式错误！！！");
        return;
      }
      if (!this.smsCodeCorrect || !this.uCode.trim()) {
        this.toast("验证码为空或格式错误！！！");
        return;
      }
      if (!this.passwordCorrect || !this.uPwd.trim()) {
        this.toast("密码为空或格式错误！！！");
        return;
      }
      if (!this.affirmPwdCorrect || !this.affirmPwd.trim()) {
        this.toast("确认密码为空或与密码不一致！！！");
        return;
      }
      let url = "orderUser/updatepassword";
      let body = {phone:this.phone,code:this.uCode, newPassword:this.uPwd};
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.toast("修改成功");
            let obj = {phone: this.phone,password:this.uPwd};
            this.pop(obj)
          }else{
            this.toast(obj.message)
          }
        },
        err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        },true
      );
    },

    
    /**
     * 获取验证码
     */
    btnClick: function() {
      if (!this.phoneCorrect || !this.phone.trim()) {
        this.toast("手机号码为空或格式错误！！！");
        return;
      }
      let num = 60;
      let url = "orderUser/smsverification";
      let body = { phone: this.phone, number: "2" };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.isBtnDis = true;
            let si = setInterval(() => {
              this.authTime = num;
              num--;
              if (num <= 0) {
                clearInterval(si);
                this.isBtnDis = false;
                this.authTime = 60;
              }
            }, 1000);
          }else{
            this.toast(obj.message)
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
  },
  watch: {
    'phone': function(val) {
      infoVerify.phone.call(this, val);
      if (!val) this.phoneCorrect = true;
    },
    'uCode': function(val) {
      infoVerify.smsCode.call(this, val);
      if (!val) this.smsCodeCorrect = true;
    },
    'uPwd': function(val) {
      infoVerify.password.call(this, val);
      if (!val) this.passwordCorrect = true;
      if(val!==this.affirmPwd)this.affirmPwdCorrect = false;
      else this.affirmPwdCorrect = true;
    },
    'affirmPwd':function(val){
      if(val!==this.uPwd)this.affirmPwdCorrect = false;
      else this.affirmPwdCorrect = true;
    },
  }
};
</script>

<style scoped>
.cs-app {
  width: 750px;
  align-items: center;
}
.scro{
  width: 750px;
}
.csFont{
  margin-top:80px;
  margin-bottom: 52px;
  text-align: center;
  font-size: 36px;
  color:rgba(51,51,51,1);
}
.inp,
.inpyz {
  margin-top: 30px;
  margin-left: 60px;
  width: 630px;
  height: 100px;
  border-radius: 3px;
  border-width: 1px;
  border-style: solid;
  border-color: rgba(204, 204, 204, 1);
  padding-left: 20px;
  font-size: 28px;
}
.inpDiv {
  margin-top: 30px;
  margin-left: 60px;
  width: 630px;
  height: 100px;
  flex-direction: row;
}
.inpyz {
  margin: 0;
  width: 430px;
}
.borCol {
  border-color: red;
}
</style>