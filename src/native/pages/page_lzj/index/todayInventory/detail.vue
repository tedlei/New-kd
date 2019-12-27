<template>
    <div :style="{flex:1}">
        <header-nav @headerEvent="headerEvent" title="快件详情" right-button />
        <scroller class="contextList">
            <div class="list">
                <div class="list_option">
                    <text class="tit">快递公司</text>
                    <text class="content">{{item.company}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">运单号</text>
                    <text class="content">{{item.orderId}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">货号</text>
                    <text class="content">{{item.shelves}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">状态</text>
                    <text class="content">{{item.orderStatus*1 === 2 ? '已出库' : '已入库'}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">客户电话</text>
                    <text class="content">{{item.expressOwnerTel}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">驿站地址</text>
                    <text class="content">{{id ? item.agentAddress : item.deliveryPhone}}</text>
                </div>
                <div class="list_option">
                    <text class="tit">入库时间</text>
                    <text class="content">{{item.handleTime}}</text>
                </div>
                <div class="list_option" v-if="item.ckTime">
                    <text class="tit">出库时间</text>
                    <text class="content">{{item.ckTime}}</text>
                </div>
            </div>
        </scroller>
    </div>
</template>

<script>
import inputFrame from "../../inform/uninformedType/input.vue";
import baseStyle from "../../../../common/baseStyle.js";

export default {
  components: { inputFrame },
  data() {
    return {
      id: true,
      baseStyle,
      item: {}
    };
  },
  methods: {},
  async created() {
    let { data } = await this.getParam();
    this.item = data;
    this.listData = await new Promise(resolve => {
      this.fetch(
        {
          url: "/manager/order/getScan",
          body: {}
        },
        result => {
          let arr = result.res.data;
          arr.sort((a, b) => {
            return a.orderStatus * 1 - b.orderStatus * 1;
          });
          resolve(arr);
        }
      );
    });

    // 身份
    this.id = await this.verdictId();
  }
};
</script>

<style scoped>
.contextList {
  flex: 1;
  width: 750px;
  padding-left: 30px;
  padding-right: 30px;
  padding-bottom: 30px;
}
.list {
  flex-direction: column;
  width: 690px;
  height: 600px;
  margin-top: 40px;
  padding-left: 20px;
  padding-right: 30px;
  margin-bottom: 40px;
  border-width: 2px;
  border-style: solid;
  border-color: rgba(230, 230, 230, 1);
  border-radius: 10px;
}
.list_option {
  flex: 1;
  flex-direction: row;
  align-items: center;
}
.tit,
.content {
  font-size: 28px;
  color: rgba(102, 102, 102, 1);
}
.tit {
  width: 340px;
}
</style>
