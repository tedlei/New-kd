<template>
  <div class="main">
    <div>
      <div class="header"></div>
      <head/>
      <mainNav/>
    </div>
  </div>
</template>

<script>
import head from './header/header.vue'
import mainNav from './nav/nav.vue'
export default {
  components: {head, mainNav},
  data() {
    return {
    };
  }
};
</script>
<style scoped>
  .main{
    flex: 1;
    justify-content: space-between;
  }
  .header{
    height: 40px;
    background-color: #0677e6;
  }
</style>
