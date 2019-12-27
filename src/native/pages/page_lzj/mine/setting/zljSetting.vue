<template>
  <div class>
    <headerNav title="滞留件设置" rightButton></headerNav>
    <!-- <div class="option" @click="cutOption(i)">
      <text style="font-size: 28px;">滞留件弹窗提示</text>
      <div class="option_right" :class="[zljPopup? 'checked' : '']">
        <text
          style="position: absolute;top:4px;left:4px;font-size: 24px;font-family: iconfont;"
          :style="{color: zljPopup ? '#fff' : 'transparent'}"
        >&#xe610;</text>
      </div>
    </div> -->
    <opt :listData="listData" :controlNum="retentionNum" HintClass="retentionHint"></opt>
    <!-- <text class="test" @click="retention">滞留件</text> -->
  </div>
</template>

<script>
import opt from "./options.vue";
export default {
  components: { opt },
  data() {
    return {
      zljPopup:true,
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
      retentionNum:0,
    };
  },
  created(){
    this.getItem("retentionNum",result=>{
      if(result.result==="success"){
        this.retentionNum = result.data*1;
      }
    })
  },
  // methods:{
  //   retention:function(){
  //      this.audio("retention", "retentionHint");
  //   }
  // }
};
</script>

<style scoped>
.option {
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  height: 100px;
  padding-left: 30px;
  padding-right: 30px;
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
}
.option:active {
  background-color: #e5e5e5;
}
.option_right {
  width: 34px;
  height: 34px;
  border-width: 2px;
  border-style: solid;
  border-color: #999;
  background-color: transparent;
  line-height: 34px;
  border-radius: 17px;
}
.checked {
  border-width: 0;
  background-color: #0077e2;
}
.test{
  width: 300px;
  height: 80px;
  text-align: center;
  line-height: 80px;
  background-color: aqua;
  font-size: 30px;
}
</style>
