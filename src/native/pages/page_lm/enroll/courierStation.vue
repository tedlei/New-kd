<template>
  <scroller class="cs-app">
    <input type="text" autofocus=true class="inp" :class="[uNmaeCorrect?'':'borCol']" max-length=8 v-model="uName" placeholder="请输入昵称，推荐使用真实名称"></input>
    <input type="number" class="inp" :class="[phoneCorrect?'':'borCol']" v-model="phone" placeholder="请输入手机号"></input>
    <div class="inpDiv">
      <input type="number" class="inpyz" :class="[smsCodeCorrect?'':'borCol']" v-model="uCode" placeholder="请输入验证码"></input>
      <wxc-button :text="isBtnDis?authTime+'秒':'获取验证码'" type='white' :btnStyle="btnStyle" @wxcButtonClicked="btnClick" :disabled="isBtnDis"></wxc-button>
    </div>
    <input v-if="selNum==1" type="text" v-model="company" class="inp" placeholder="请输入所属公司名称"></input>
    <input v-if="selNum==1" type="text" v-model="storeLoca" class="inp" placeholder="请输入派件范围"></input>
    <input v-if="selNum==2" type="text" v-model="storeLoca" class="inp" placeholder="请输入店铺地址"></input>
    <input type="password" class="inp" :class="[passwordCorrect?'':'borCol']" v-model="uPwd" placeholder="请输入您的密码"></input>
    <wxc-button text="注册" @wxcButtonClicked='enroll' class="inp" :btnStyle="btnStyle"></wxc-button>
  </scroller>
</template>
<script>
import { WxcButton } from "weex-ui";
import infoVerify from "../../../common/infoVerify.js";
export default {
  components: { WxcButton },
  props: ["selNum"],
  data() {
    return {
      uName: "", //用户名
      phone: "", //电话
      uCode: "", //验证码
      company: "", //公司
      storeLoca: "", //店铺地址
      uPwd: "", //密码

      btnStyle: {
        //页面按钮样式
        width: "200px",
        height: "100px",
        backgroundColor: "rgba(0,119,226,1)",
        borderRadius: "3px",
        color: "rgba(255,255,255,1)"
      },
      authTime: 60, //获取验证码倒计时
      isBtnDis: false, //禁用按钮

      uNmaeCorrect: true, //验证用户名
      phoneCorrect: true, //验证电话
      smsCodeCorrect: true, //验证验证码
      passwordCorrect: true //验证密码
    };
  },
  methods: {
    /**
     * 点击注册
     */
    enroll: function() {
      if (!this.uNmaeCorrect) {
        this.toast("用户名为空或格式错误！！！");
        return;
      }
      if (!this.phoneCorrect || !this.phone.trim()) {
        this.toast("手机号码为空或格式错误！！！");
        return;
      }
      if (!this.smsCodeCorrect || !this.uCode.trim()) {
        this.toast("验证码为空或格式错误！！！");
        return;
      }
      if (this.selNum === 2) {
        if (!this.storeLoca.trim()) {
          this.toast("店铺地址不能为空！！！");
          return;
        }
      } else {
        if (!this.company.trim()) {
          this.toast("公司名称不能为空！！！");
          return;
        }
      }
      if (!this.passwordCorrect || !this.uPwd.trim()) {
        this.toast("密码为空或格式错误！！！");
        return;
      }
      let url = "orderUser/register";
      let body = {
        code: this.uCode,
        phone: this.phone,
        password: this.uPwd,
        username: this.uName,
        company: this.company,
        address: this.storeLoca,
        role: this.selNum,
        number:"1"
      };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.toast("注册成功");
            let obj = {phone: this.phone,password: this.uPwd}
            this.pop(obj)
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
      let body = { phone: this.phone, number: "1" };
      this.fetch({url,body},res => {
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
        }else if(obj.code===400){
          this.toast(obj.message);
        }
      },err => {
        if (!err) {
          this.toast("网络错误,请检查您的网络！！！",err);
        }
      },true);
    }
  },
  watch: {
    uName: function(val) {
      let str = val.trim();
      if (!str) this.uNmaeCorrect = false;
      else this.uNmaeCorrect = true;
    },
    phone: function(val) {
      infoVerify.phone.call(this, val);
      if (!val) this.phoneCorrect = true;
    },
    uCode: function(val) {
      infoVerify.smsCode.call(this, val);
      if (!val) this.smsCodeCorrect = true;
    },
    uPwd: function(val) {
      infoVerify.password.call(this, val);
      if (!val) this.passwordCorrect = true;
    },
    storeLoca: function(val) {
      let str = val.trim();
      if (!str) this.storeLocaCorrect = false;
      else this.storeLocaCorrect = true;
    }
  }
};
</script>

<style scoped>
.cs-app {
  margin-top: 20px;
  width: 750px;
  align-items: center;
}
.inp,
.inpyz {
  margin-top: 30px;
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