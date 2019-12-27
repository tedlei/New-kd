<template>
  <div style="flex: 1;">
    <div class="head">
      <text style="color: #333;font-size: 30px;">充值总金额：{{allout || 0 }}&nbsp;元</text>
<!--      <text style="color: #333;font-size: 30px;">赠送金额：{{allrepertory || 0 }}&nbsp;元</text>-->
    </div>
    <div class="main">
      <text class="main_tit pl30">充值记录</text>
      <scroller>
        <div class="main_list pl30" v-for="(item, i) in list" :key="i">
          <div class="main_list_top">
            <text class="main_list_text" style="width: 398px;color: #333;">用户名：{{item.userPhone}}</text>
            <text class="main_list_text" style="color: #333;">充值金额：{{item.moneyNum}}</text>
          </div>
          <text class="main_list_text" style="color: #999;">发送时间：{{item.moneyTime}}</text>
        </div>
      </scroller>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  props: ['list', 'allout', 'allrepertory'],
  watch: {
    beginDate(newValue){
    },
    endDate(newValue){
    }
  }
};
</script>

<style src="./base.css" scoped></style>
