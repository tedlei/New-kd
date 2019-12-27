<template>
  <div class>
    <headerNav title="退件设置" rightButton></headerNav>
    <opt :listData="listData" :controlNum="handBackNum" HintClass="handBackHint"></opt>
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
      handBackNum: 0
    };
  },
  created() {
    this.getItem("handBackNum", result => {
      if (result.result === "success") {
        this.handBackNum = result.data * 1;
      }
    });
  },
  // methods: {
  //   handBackSucceed: function() {
  //     this.audio("handBackSucceed", "handBackHint");
  //   },
  //   handBackFailed: function() {
  //     this.audio("handBackFailed", "handBackHint");
  //   }
  // }
};
</script>

<style scoped>
.test {
  width: 300px;
  height: 80px;
  text-align: center;
  line-height: 80px;
  background-color: aqua;
  font-size: 30px;
}
</style>
