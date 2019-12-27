<template>
  <div :style="{flex:1}">
    <header-nav @headerEvent="headerEvent" title="已通知" right-button />
    <inputFrame @select="select" style="margin-top:20px;margin-bottom:20px;"></inputFrame>
    <div style="flex: 1;justify-content: space-between;">
      <scroller style="flex: 1;">
        <div class="list" v-for="(item, i) in listData" :key="i">
          <div class="list_top">
            <text
              style="color: #333333;"
              :style="baseStyle.fs30"
            >{{item.company}}&nbsp;&nbsp;{{item.orderId}}</text>
            <text style="color: #999999;" :style="baseStyle.fs30">货号&nbsp;&nbsp;{{item.shelves}}</text>
          </div>
          <div class="list_bottom">
            <text style="color: #999999;" :style="baseStyle.fs30">电话&nbsp;&nbsp;{{item.expressOwnerTel}}</text>
          </div>
        </div>
      </scroller>
      <div v-if="listData.length" class="list_last" :style="baseStyle.fCenter">
        <text :style="baseStyle.fs30" style="color: #999999;">共计: {{listData.length}}条</text>
      </div>
    </div>
  </div>
</template>

<script>
import inputFrame from "../uninformedType/input.vue";
import baseStyle from "../../../../common/baseStyle.js";
import findInformData from '../findInformData';

export default {
    components: { inputFrame },
    data() {
        return {
            baseStyle,
            listData: []
        };
    },
    methods: {
        headerEvent() {},
        async select(value){
            this.listData = await findInformData.find.call(this, value, 2);
            this.toast(this.listData.length ? '查询成功' : '未查找到数据');
        }
    },
    async created(){
      this.listData = await findInformData.init.call(this, 2);
    }
};
</script>

<style scoped>
.list {
  justify-content: space-between;
  height: 118px;
  padding: 18px 28px;
  background-color: #fff;
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: rgba(230, 230, 230, 1);
}
.list:active {
  background-color: #f1f1f1;
}
.list_top,
.list_bottom {
  flex-direction: row;
  justify-content: space-between;
  width: 694px;
}
.list_last {
  height: 124px;
  background-color: #fff;
  border-width: 2px;
  border-style: solid;
  border-color: #f1f1f1;
}
</style>
