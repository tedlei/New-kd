<template>
  <div class>
    <headerNav title="模板审核设置" rightButton></headerNav>
    <opt :listData="listData"></opt>
  </div>
</template>

<script>
import opt from "./options.vue";
export default {
  components: { opt},
  data() {
    return {
      listData: [
        {
          text: "声音",
          status: true
        },
        {
          text: "振动",
          status: true
        }
      ]
    };
  }
};
</script>

<style scoped>
</style>
