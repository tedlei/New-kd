<template>
  <div style="flex: 1;">
    <div class="head">
      <text style="width: 690px;text-align: center;color: #333;font-size: 30px;">共：{{list.length}}条</text>
    </div>
    <div class="main">
      <text class="main_tit pl30">消费记录</text>
      <scroller>
        <div class="main_list pl30" v-for="(item, i) in list" :key="i">
          <div class="main_list_top">
            <text class="main_list_text" style="width: 398px;color: #333;">{{item.orderId ? '运单号' : '客户电话'}}：{{item.orderId || item.orderPhone}}</text>
            <text class="main_list_text" style="color: #333;">{{item.moneyConsume}}</text>
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
};
</script>

<style src="./base.css" scoped></style>
