<template>
  <div class="qp-app">
    <headerNav :title = "typeNum===2?'出库查询':'查询'" rightButton=""></headerNav>
    <div class="qpList">
      <div class="qpQuery" :style="bd.borderWS">
        <input class="qpInp" v-model="val" type="text" :autofocus="true" placeholder="请输入运单号、手机号查询"></input>
        <text class="qpText" @click="topQuery">查询</text>
      </div>
    </div>
    <text class="qp_text_style" v-if="queryList.length>0">{{wzts}} &nbsp;&nbsp; 未取快递{{count}}个</text>

    <scroller class="scrHeight" v-if="queryList.length>0">
      <div v-for="(e,i) of queryList" :key="i" >
          <inquireDetail :typeNum="typeNum" @topQuery="topQuery" :obj="e" :sequence="i"></inquireDetail>
      </div>
    </scroller>
    <text class="text_font" v-else>没有数据！！</text>

  </div>
</template>

<script>
import inquireDetail from "./inquireDetail.vue";
import bd from "../../../common/baseStyle.js";
export default {
  components: { inquireDetail },
  data() {
    return {
      bd,
      val: "", //查询条件
      queryList: [], //查询结果
      typeNum: 1, //1：查询跳转     2：出库跳转
      wzts: ""
    };
  },
  async created() {
    let value = await this.getParam();
    if (value.num) {
      this.typeNum = value.num * 1;
      return;
    }
    if (value.type) this.typeNum = value.type * 1;
    if (value.orderNum) {
      this.val = value.orderNum;
      this.topQuery();
    }
  },
  methods: {
    //点击查询时
    topQuery: function() {
      this.queryList = [];
      let url = "manager/order/find";
      let body = { orderId: this.val };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            let arr = obj.data;
            if (arr.length > 0) {
              if (this.typeNum === 2)
                this.playAudio(obj.data[0].orderStatus * 1, 1);
              this.queryList = arr;
              let value = this.val;
              if (value.length === 11) this.wzts = "电话：" + value;
              else if (value.length === 4)
                this.wzts = "电话：" + this.queryList[0].expressOwnerTel;
              else if (value.length === 8) this.wzts = "货号：" + value;
              else if (value.length > 11) this.wzts = "运单号：" + value;
              else this.wzts = "姓名：" + value;
            } else this.toast("未查询到相关的快件信息！！");
          } else if (obj.code === 401) {
            this.toast(obj.message);
          }
        },
        err => {
          this.toast("网络错误,请检查您的网络！！！");
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
    count: function() {
      let num = 0;
      for (let val of this.queryList) {
        let zt = val.orderStatus * 1;
        if (zt === 1 || zt === 3) num++;
      }
      return num;
    }
  }
};
</script>

<style scoped>
.qp-app {
  flex: 1;
}
.qpList {
  width: 750px;
}
.qpQuery {
  margin-top: 20px;
  margin-left: 40px;
  width: 690px;
  height: 88px;
  border-color: rgba(204, 204, 204, 1);
  border-radius: 10px;
  flex-direction: row;
  justify-content: space-between;
}
.qpInp {
  width: 500px;
  height: 84px;
  padding-left: 10px;
  font-size: 32px;
}
.qpText {
  width: 180px;
  height: 88px;
  line-height: 88px;
  text-align: center;
  font-size: 40px;
  color: rgba(255, 255, 255, 1);
  background-color: rgba(0, 119, 226, 1);
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
}
.qp_text_style {
  margin-top: 20px;
  margin-left: 40px;
  font-size: 28px;
  color: rgba(226, 0, 0, 1);
}
.scrHeight {
  margin-bottom: 10px;
  margin-left: 40px;
  width: 670px;
  flex: 1;
}
.text_font {
  margin-top: 40px;
  text-align: center;
  font-size: 30px;
}
</style>
