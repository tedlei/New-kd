<template>
  <div class>
    <headerNav title="入库设置" rightButton></headerNav>
    <opt :listData="listData" :controlNum="placeHintNum" HintClass="placeHint"></opt>
    <!-- <text class="test" @click="pctcInfo">请核实快件信息</text>
    <text class="test" @click="placeSucceed">放置成功</text>
    <text class="test" @click="placeFailed">放置失败</text>
    <text class="test" @click="repetition">重复</text>
    <text class="test" @click="enterIntoSucceed">入库成功</text>
    <text class="test" @click="enterIntoFailed">入库失败</text> -->
  </div>
</template>

<script>
import opt from "./options.vue"
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
      placeHintNum:0,
    };
  },
   created(){
    this.getItem("placeHintNum",result=>{
      if(result.result==="success"){
        this.placeHintNum = result.data*1;
      }
    })
  },
  // methods:{
  //   pctcInfo:function(){
  //      this.audio("pctcInfo", "placeHint");
  //   },
  //   placeSucceed:function(){
  //      this.audio("placeSucceed", "placeHint");
  //   },
  //   placeFailed:function(){
  //      this.audio("placeFailed", "placeHint");
  //   },
  //   repetition:function(){
  //      this.audio("repetition", "placeHint");
  //   },
  //   enterIntoSucceed:function(){
  //      this.audio("enterIntoSucceed", "placeHint");
  //   },
  //   enterIntoFailed:function(){
  //      this.audio("enterIntoFailed", "placeHint");
  //   }
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
