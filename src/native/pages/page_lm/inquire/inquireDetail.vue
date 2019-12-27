<template>
  <scroller class="ql-app">
    <text class="ql_text_style">第{{num}}单</text>
    <div class="qlul">
        <div class="qlli">
            <text class="fontStyle">运单号</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle">{{expressage.orderId}}</text>
        </div>
        <template v-if="userId===2">
          <div class="qlli">
              <text class="fontStyle">快递公司</text>
          </div>
          <div class="qlli qlli1">
              <text class="fontStyle">{{expressage.company}}</text>
          </div>
        </template>
        <template v-if="expressage.shelves">
          <div class="qlli">
              <text class="fontStyle">货号</text>
          </div>
          <div class="qlli qlli1">
              <text class="fontStyle">{{expressage.shelves}}</text>
          </div>
        </template>
        <template v-if="userId===1&&expressage.agentId">
          <div class="qlli">
              <text class="fontStyle">代办点电话</text>
          </div>
          <div class="qlli qlli1">
              <text class="fontStyle">{{expressage.agentId}}</text>
          </div>
        </template>
        <template v-if="userId===1&&expressage.agentAddress">
          <div class="qlli">
              <text class="fontStyle">代办点地址</text>
          </div>
          <div class="qlli qlli1">
              <text class="fontStyle">{{expressage.agentAddress}}</text>
          </div>
        </template>
        <template v-if="userId===2">
            <template v-if="expressage.userName">
                <div class="qlli">
                    <text class="fontStyle">快递员姓名</text>
                </div>
                <div class="qlli qlli1">
                    <text class="fontStyle">{{expressage.userName}}</text>
                </div>
            </template>

            <template v-if="expressage.userId">
                <div class="qlli">
                    <text class="fontStyle">快递员电话</text>
                </div>
                <div class="qlli qlli1">
                    <text class="fontStyle">{{expressage.userId}}</text>
                </div>
            </template>
        </template>
        <div class="qlli">
            <text class="fontStyle">用户姓名</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle">{{expressage.nickName?expressage.nickName:"未填写"}}</text>
        </div>
        <div class="qlli">
            <text class="fontStyle">用户电话</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle">{{expressage.expressOwnerTel}}</text>
        </div>
        <div class="qlli">
            <text class="fontStyle">入库时间</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle">{{expressage.handleTime}}</text>
        </div>
        <div class="qlli">
            <text class="fontStyle">在库时间</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle">{{expressage.day}}小时</text>
        </div>
        <template v-if="expressage.ckTime">
          <div class="qlli" >
              <text class="fontStyle">出库时间</text>
          </div>
          <div class="qlli qlli1">
              <text class="fontStyle">{{expressage.ckTime}}</text>
          </div>
        </template>
        <div class="qlli">
            <text class="fontStyle">出库状态</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle" :class="[expressage.orderStatus==='5'||expressage.orderStatus==='6'?'colorRed':'']">{{ztDs}}</text>
        </div>
        <div class="qlli">
            <text class="fontStyle">是否通知</text>
        </div>
        <div class="qlli qlli1">
            <text class="fontStyle">{{expressage.smsStatus==2?'已':'未'}}通知</text>
        </div>
        <div class="flex_text" v-if="typeNum===2">
            <div class="qlli">
                <text class="fontStyle">备注</text>
            </div>
            <div class="qlli qlli1 flex_text">
                <input type="text" v-model="remark" :style="bd.borderWS" 
                placeholder="备注" class="inp_text"
                v-if="userId===2&&(expressage.orderStatus==='1'||expressage.orderStatus==='3')"
                ></input>
                <text v-else class="fontStyle">{{expressage.orderRemark?expressage.orderRemark:'无'}}</text>
            </div>
        </div>
    </div>
    <template v-if="userId===2">
        <text :style="bd.borderWS" v-if="typeNum===2&&expressage.orderStatus==='1'" @click="topCk(1)" class="notarize">取件出库</text>
        <div class="qlula"  v-if="typeNum===1&&expressage.orderStatus==='1'">
            <div class="qllia" v-if="expressage.orderStatus!=='3'">
                <div @click="topUpd('upd')"><text class="fontStyle textal">修改</text></div>
            </div>
            <div class="qllia leftBd" v-if="expressage.orderStatus!=='3'">
                <div @click="delivery(2)"><text class="fontStyle textal">扫码取件</text></div>
            </div>
            <div class="qllia leftBd">
                <div @click="delivery(4)"><text class="fontStyle textal">扫码退件</text></div>
            </div>
        </div>
    </template>
  </scroller>
</template>

<script>
import bd from "../../../common/baseStyle.js";
export default {
  props: ["obj","sequence","typeNum"],
  data() {
    return {
      bd,
      expressage: {},
      userInfo: {},
      remark:'',
      userId: 1,
    };
  },
  mounted(){
    this.expressage = this.obj;
    this.getItem("userInfo", res => {
      if (res) {
        this.userId = res.data.userRole * 1;
        this.userInfo = res.data;
      }
    });
  },
  methods: {
    //跳转修改
    topUpd: function() {
      this.navigator.pushFull({url:"root:pages/page_lm/inquire/update.js",param:{ expressage: this.expressage }},()=>{
        this.$emit("topQuery")
      });
    },

    //跳转扫码
    delivery: function(num) {
      let ui = this.userInfo;
      if (num === 2) {
        this.pushAndroid("openOutLibActivity", [ui.sessionId, ui.userRole]);
      } else {
        this.pushAndroid("openOutPackageActivity", [ui.sessionId, ui.userRole]);
      }
    },

    //快递状态切换
    topCk: function(num) {
      let url, body;
      url = "manager/order/update";
      body = { orderId: this.expressage.orderId, remark: this.remark }; 
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            this.expressage = obj.data[0];
            this.playAudio(2, 2);
          } else {
            this.toast(obj.message);
            this.playAudio(0, 2);
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！！！");
          this.playAudio(0, 2);
        },
        true
      );
    },
    //判断调用出库提示音
    playAudio: function(type, num) {
      if (num === 0) return;
      if (num === 1) {
        if (type === 1) this.audio("pvtpInfo", "goOutHint");
        else if (type === 2 || type === 4)
          this.audio("alreadyGoOut", "goOutHint");
      } else {
        if (type === 2) this.audio("goOutSucceed", "goOutHint");
        else if (!type) this.audio("goOutFailed", "goOutHint");
      }
    }
  },
  computed: {
    ztDs: function() {
      let num = this.expressage.orderStatus;
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
          str = "已放置";
          break;
      }
      if(num==='1'&&!this.expressage.agentAddress)return '代办点未入库'
      return str;
    },
    num:function(){
      let se = this.sequence;
      return se+1>9?se+1:'0'+(se+1);
    }
  }
};
</script>

<style scoped>
.bd {
  border-width: 1px;
  border-style: solid;
}
.ql_text_style{
  margin-top: 50px;
  text-align: center;
  font-size: 30px;
  color:red;
}
.qlul {
  /* margin-left: 30px; */
  margin-top: 25px;
  /* padding-bottom: 30px; */
  /* width: 690px; */
  /* border-color: rgba(230, 230, 230, 1); */
    border-top-style: solid;
  border-top-width: 1px;
  border-top-color: rgba(204,204,204,0.8);

  /* border-radius: 10px; */
  flex-direction: row;
  flex-wrap: wrap;
}
.qlli {
  margin-top: 30px;
  /* margin-left: 24px; */
  width: 200px;
}
.qlli1 {
  width: 400px;
}
.fontStyle {
  font-size: 32px;
  color: rgba(102, 102, 102, 1);
}
.qlula {
  /* margin-left: 30px; */
  margin-top: 32px;
  /* width: 690px; */
  height: 80px;
  border-color: #666;
  border-top-width: 2px;
  border-bottom-width: 2px;
  border-style: solid;
  flex-direction: row;
  justify-content: center;
}
.qllia {
  /* width: 230px; */
  flex:1;
  height: 80px;
}
.textal {
  text-align: center;
  line-height: 80px;
  color: rgba(102, 102, 102, 1);
}
.leftBd {
  border-left-color: #666;
  border-left-style: solid;
  border-left-width: 2px;
}
.btnDiv {
  margin-top: 30px;
  margin-left: 262px;
  width: 230px;
  height: 80px;
  background-color: rgba(204, 204, 204, 1);
}
.cen {
  text-align: center;
  line-height: 80px;
  color: rgba(51, 51, 51, 1);
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
.flex_text{
  flex-direction: row;
  align-items: center;
}
.inp_text{
  width: 400px;
  height: 60px;
  padding-left: 15px;
}
.colorRed{
  color:red;
}
</style>
