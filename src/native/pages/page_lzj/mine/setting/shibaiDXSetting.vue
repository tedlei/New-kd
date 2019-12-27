<template>
  <div class>
    <headerNav title="失败短信提醒" rightButton></headerNav>
    <opt :listData="listData"></opt>
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
          text: "弹窗",
          status: true
        }
      ]
    };
  }
};
</script>

<style scoped>
</style>
