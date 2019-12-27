<template>
  <div class>
    <headerNav title="出库设置" rightButton></headerNav>
    <opt :listData="listData" :controlNum="goOutHintNum" HintClass="goOutHint"></opt>

    <!-- <text class="test" @click="pvtpInfo">请核实取件者信息</text>
    <text class="test" @click="goOutSucceed">出库成功</text>
    <text class="test" @click="goOutFailed">出库失败</text>
    <text class="test" @click="alreadyGoOut">已出库</text> -->
  </div>
</template>

<script>
import opt from "./options.vue";

export default {
  components: { opt },
  data() {
    return {
      listData: [
        {
          text: "语音提示",
          status: true
        },
        {
          text: "“滴”声提示",
          status: false
        },
        {
          text: "无提示音",
          status: false
        }
      ],
      goOutHintNum:0,
    };
  },
  created(){
    this.getItem("goOutHintNum",result=>{
      if(result.result==="success"){
        this.goOutHintNum = result.data*1;
      }
    })
  },
  // methods:{
  //   pvtpInfo:function(){
  //      this.audio("pvtpInfo", "goOutHint");
  //   },
  //   goOutSucceed:function(){
  //      this.audio("goOutSucceed", "goOutHint");
  //   },
  //   goOutFailed:function(){
  //      this.audio("goOutFailed", "goOutHint");
  //   },
  //   alreadyGoOut:function(){
  //      this.audio("alreadyGoOut", "goOutHint");
  //   },
  // }
};
</script>

<style scoped>
.test{
  width: 300px;
  height: 80px;
  text-align: center;
  line-height: 80px;
  background-color: aqua;
  font-size: 30px;
}
</style>
