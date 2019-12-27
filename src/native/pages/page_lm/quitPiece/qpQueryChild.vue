<template>
  <div class="qp-app">
    <headerNav title = "退件查询" rightButton=""></headerNav>
    <div class="qpList">
      <div class="qpQuery" :style="bd.borderWS">
        <input class="qpInp" v-model="val" :autofocus='true' placeholder="请输入运单号、手机号、姓名、货号"></input>
        <text class="qpText" @click="topQuery">查询</text>
      </div>

      <scroller class="scrHeight" v-if="queryList.length>0">
        <div v-for="(e,i) of queryList" :key="i" >
          <courierList :obj="e" @topQuery="topQuery" :userRole="userRole"></courierList>
        </div>
      </scroller>
      <text class="text_font" v-else>没有数据！！</text>
    </div>
  </div>
</template>

<script>
import courierList from "../courierList/courierList.vue";
import bd from "../../../common/baseStyle.js";
import backKeyEvent from "../../../common/backKeyEvent.js";

export default {
  components: { courierList },
  data() {
    return {
      bd,
      val: "", //查询条件
      queryList: [], //查询结果
      userRole: 1, //是否为快递员
    };
  },
  mounted(){
    this.getItem('userInfo',res=>{
      if(res.result==='success') this.userRole = res.data.userRole*1
    })
  },
  methods: {
    //点击查询时
    topQuery: function() {
      this.queryList = [];
      let url = "manager/order/find";
      let body = { orderId: this.val, yesandno: 99 };
      this.fetch({ 
          url, 
          body 
        }, res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (obj.data.length > 0) {
              this.queryList=obj.data;
            } else this.toast("未查询到该快件信息");
          } else{
            this.toast(obj.message);
          }
        }, err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        },true);
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
  flex: 1;
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
.scrHeight {
  width: 750px;
  flex: 1;
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
.text_font{
  margin-top: 40px;
  text-align: center;
  font-size: 30px;
}
</style>
