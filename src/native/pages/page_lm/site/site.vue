<template>
  <div class="site-app flex1">
    <headerNav title="添加代办点地址" rightButton></headerNav>
    <div class="site_inp">
      <input class="site_input" v-model="siteValue" ref="input1" :style="bd.borderWS" placeholder="请输入电话或详细地址"></input>
      <text class="site_text_btn" @click="topQuery">添加</text>
    </div>

    <div class='flex1'>
      <div class='flex1'>
        <text class="site_text_font">已注册的代办点</text>
        <scroller class="site_scro flex1" :style="bd.borderWS">
          <div class="site_list" v-for="(e,i) of yetSiteList" :key="i" @longpress="LongPress(0,i+1)">
            <text class="site_list_text flex1">{{e}}</text>
            <text class="iconfont site_icon" @click="removeSiteList(e,1)" v-if="classNum===0&&listNum===(i+1)">&#xe613;</text>
          </div>
        </scroller>
      </div>
      <div class='flex1'>
        <text class="site_text_font" style="height:35px;">未注册的代办点</text>
        <scroller class="site_scro flex1" :style="bd.borderWS">
          <div class="site_list" v-for="(e,i) of yetSiteList1" :key="i" @longpress="LongPress(1,i+1)">
            <text class="site_list_text flex1">{{e}}</text>
            <text class="iconfont site_icon" @click="removeSiteList(e,2)" v-if="classNum===1&&listNum===(i+1)">&#xe613;</text>
          </div>
        </scroller>
      </div>
    </div>

    <text style="height:20px;"></text>
  </div>
</template>

<script>
import bd from "../../../common/baseStyle.js";
let modal = weex.requireModule("modal");
export default {
  data() {
    return {
      bd,
      siteValue: "",
      yetSiteList: [],
      yetSiteList1: [],
      classNum: 0,
      listNum: 0,
      userInfo: {}
    };
  },
  mounted() {
    this.getItem("userInfo", res => {
      if (res.result === "success") {
        this.userInfo = res.data;
      }
    });
    this.querySiteList();
  },
  methods: {
    LongPress: function(num, num1) {
      //从这儿开始
      if (this.classNum === num && this.listNum === num1) {
        this.listNum = 0;
        return;
      }
      this.classNum = num;
      this.listNum = num1;
    },
    //打开界面查询
    querySiteList: function() {
      let url = "orderAddress/getUserAddress";
      this.fetch(
        { url, body: {} },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.yetSiteList = obj.data;
            this.yetSiteList1 = obj.data1;
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
    //输入查询
    topQuery: function() {
      if (!this.siteValue.trim()) {
        this.toast("请输入内容！！");
        return;
      }
      if (this.siteValue === this.userInfo.userPhone) {
        this.toast("不能添加自己的地址！！！");
        return;
      }
      let url = "orderAddress/selectAddress";
      let body = { Phone: this.siteValue };
      let boole = /[\u4e00-\u9fa5]/.test(this.siteValue.trim());
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.judge(obj.data, 1);
          }
          else {
            if (boole) this.judge(this.siteValue, 2);
            else{
              this.toast("没有该代办点！！");
              this.$refs["input1"].blur();
            }
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！！！");
        },
        true
      );
    },
    //判断是否重复
    judge: function(address, index) {
      let number = 2;
      number = this.yetSiteList.indexOf(address) + number;
      number = this.yetSiteList1.indexOf(address) + number;
      if (number) {
        this.toast("已添加该代办点，请勿重复添加！！");
        return;
      }
      if (index === 1) {
        let str = "该地址:" + address + "\n已在本平台注册，是否添加";
        modal.confirm(
          { message: str, okTitle: "确认", cancelTitle: "取消" },
          res => {
            if (res === "确认") this.addSiteList(address, 1);
            else this.toast("用户取消!!");
            this.$refs["input1"].blur();
          }
        );
      } else {
        let str = "该地址:" + address + "\n未在本平台注册，是否添加";
        modal.confirm(
          { message: str, okTitle: "确认", cancelTitle: "取消" },
          res => {
            if (res === "确认") this.addSiteList(address, 2);
            else this.toast("用户取消!!");
            this.$refs["input1"].blur();
          }
        );
      }
    },
    //添加
    addSiteList: function(address, index) {
      let url = "orderAddress/setUserAddress";
      let body = { address, index };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (index === 1) this.yetSiteList.push(address);
            else this.yetSiteList1.push(address);
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
    //删除
    removeSiteList: function(address, index) {
      modal.confirm(
        { message: "是否删除", okTitle: "确认", cancelTitle: "取消" },
        res => {
          if (res === "确认") this.remove(address, index);
          else {
            this.toast("用户取消!!");
            this.listNum = 0;
          }
          this.$refs["input1"].blur();
        }
      );
    },
    
    remove: function(address, index) {
      let url = "orderAddress/removeAddress";
      let body = { address, index };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (index === 1) {
              var num = this.yetSiteList.indexOf(address);
              if (num > -1) {
                this.yetSiteList.splice(num, 1);
                this.listNum = 0;
              }
            } else {
              var num = this.yetSiteList1.indexOf(address);
              if (num > -1) {
                this.yetSiteList1.splice(num, 1);
                this.listNum = 0;
              }
            }
          } else {
            this.toast(obj.message);
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！！！");
        },
        true
      );
    }
  }
};
</script>

<style scoped>
.flex1 {
  flex: 1;
}
.site-app {
  width: 750px;
}
.site_inp {
  margin-top: 20px;
  margin-left: 28px;
  margin-right: 28px;
  flex-direction: row;
  justify-content: space-between;
}
.site_input {
  width: 496px;
  height: 88px;
  padding-left: 22px;
  border-color: rgba(204, 204, 204, 1);
  border-radius: 10px;
  font-size: 34px;
}
.site_text_btn {
  width: 176px;
  height: 88px;
  background-color: rgba(0, 119, 226, 1);
  border-radius: 10px;
  text-align: center;
  line-height: 88px;
  font-size: 34px;
  color: white;
}
.site_text_font {
  margin-top: 44px;
  padding-left: 32px;
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.site_scro {
  margin-top: 40px;
  margin-left: 30px;
  width: 690px;
  border-color: rgba(204, 204, 204, 1);
  border-radius: 10px;
}
.site_list {
  height: 94px;
  padding-left: 40px;
  border-bottom-style: solid;
  border-bottom-width: 2px;
  border-bottom-color: rgba(204, 204, 204, 1);
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.site_list:active {
  background-color: rgba(204, 204, 204, 1);
}
.site_list_text {
  font-size: 34px;
  color: rgba(102, 102, 102, 1);
}
.site_icon {
  width: 94px;
  height: 92px;
  text-align: center;
  line-height: 92px;
  color: red;
  font-family: iconfont;
}


.aaaaa{
  background-color: aqua;
}
</style>
