<template>
  <div class="login-app">
    <image class="loginImg" src="root:img/login.jpg"></image>
    <text class="loginFont">收发裹驿站</text>
    <div class="loginInp" :class="[phoneCorrect?'':'borCol']" :style="bd.borderWS">
      <input class="loginInput" v-model="telNo" type="number" :autofocus="false" placeholder="请输入帐户名"></input>
    </div>
    <div class="loginInp" :class="[passwordCorrect?'':'borCol']" :style="bd.borderWS">
      <input class="loginInput" v-model="password" type="password" placeholder="请输入帐密码"></input>
    </div>
    <div class="loginwjma">
      <text class="loginzcwj" @click="topzcpw(1)">账号注册</text>
      <text class="loginzcwj" @click="topzcpw(2)">忘记密码？</text>
    </div>
    <text class="loginBtn" @click="topLogin">登录</text>
  </div>
</template>

<script>
import bd from "../../../common/baseStyle.js";
import infoVerify from "../../../common/infoVerify.js";
import backKeyEvent from "../../../common/backKeyEvent";
export default {
  data() {
    return {
      bd,
      telNo: "", //帐户名
      password: "", //密码
      phoneCorrect: true, //账户名验证
      passwordCorrect: true //验证密码
      // user: {}
    };
  },
  async created() {
    // 自定义返回键事件
    await backKeyEvent.androidEnableExit.call(this);
  },
  methods: {
    //点击注册或忘记密码
    topzcpw: function(num) {
      let url;
      if (num === 1) url = "root:pages/page_lm/enroll/enroll.js";
      else url = "root:pages/page_lm/forgetPwd/forgetPwd.js";

      this.navigator.pushFull({ url }, res => {
        if (res) {
          this.telNo = res.phone;
          this.password = res.password;
        }
      });
    },

    //点击登陆时
    topLogin: function() {
      if (!this.phoneCorrect || !this.telNo.trim()) {
        this.toast("手机号码为空或格式错误！！！");
        return;
      }
      if (!this.passwordCorrect || !this.password.trim()) {
        this.toast("密码为空或格式错误！！！");
        return;
      }
      let url = "orderUser/login";
      let body = { telNo: this.telNo, password: this.password };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.toast("登陆成功");
            this.registerAudio();
            this.setItem("token", obj.data.sessionId);
            this.setItem("userInfo", obj.data);
            this.push("index",{number:obj.data1});
            this.aa()
          } else {
            this.toast(obj.message);
          }
        },
        err => {
          /**
           *
           */
          this.toast('系统错误，请重新登陆');
          this.setItem('baseUrl', 'http://39.108.91.49:8080/');
        },true);
    },

    //获取短信模板
    aa:function(){
      this.setItem("smsList",[]);
      let url = 'orderSmstemplate/getsms';
      let body = {} ;
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code=== 200) {
            this.cc(obj.data)
          } else if (obj.code === 401) {
          }
        },
        err => {
            this.toast("网络错误,请检查您的网络！！！");
        },true
      );
    },

    //获取数据处理
    cc:function(obj){
      let arr1 = [];
      let arr2 = [];
      for(let o of obj){
        if(o.smsStatus==="2")arr2.push(o);
        else arr1.push(o);
      }
      this.smsList = [arr1,arr2];
      this.setItem("smsList",this.smsList)
    }
  },
  watch: {
    telNo: function(val) {
      infoVerify.phone.call(this, val);
      if (!val) this.phoneCorrect = true;
    },
    password: function(val) {
      infoVerify.password.call(this, val);
      if (!val) this.passwordCorrect = true;
    }
  }
};
</script>

<style scoped>
.login-app {
  flex: 1;
}
.loginImg {
  margin-top: 116px;
  margin-left: 312px;
  width: 126px;
  height: 124px;
}
.loginFont {
  margin-top: 28px;
  text-align: center;
  font-size: 36px;
  color: rgba(51, 51, 51, 1);
}
.loginInp {
  margin-top: 50px;
  margin-left: 60px;
  width: 630px;
  height: 100px;
  padding-left: 24px;
  border-color: rgba(204, 204, 204, 1);
  border-radius: 6px;
}
.loginInput {
  width: 600px;
  height: 96px;
  font-size: 32px;
  color: rgba(51, 51, 51, 1);
}
.loginwjma {
  margin-left: 60px;
  width: 630px;
  flex-direction: row;
  justify-content: space-between;
}
.loginzcwj {
  height: 100px;
  line-height: 100px;
  font-size: 28px;
  color: rgba(0, 119, 226, 1);
}
.loginBtn {
  margin-top: 100px;
  margin-left: 60px;
  width: 630px;
  height: 88px;
  background-color: rgba(0, 119, 226, 1);
  border-radius: 6px;
  text-align: center;
  line-height: 88px;
  font-size: 40px;
  color: rgba(255, 255, 255, 1);
}
.borCol {
  border-color: red;
}
</style>
