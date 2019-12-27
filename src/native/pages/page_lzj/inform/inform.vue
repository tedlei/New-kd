<template>
  <div>
    <header-nav @headerEvent="headerEvent" title="通知" right-button="" />
    <div v-if="id" class="tit">
      <text class="titText">今日入库：</text>
      <text class="titText"
            style="font-weight: bold;padding-right: 32px;">{{dataList[0]}}</text>
      <text class="titText">总库存量：</text>
      <text class="titText"
            style="font-weight: bold;">{{dataList[1]}}</text>
    </div>
    <mainList :nav_main_list="nav_main_list" option-size="250px"></mainList>
  </div>
</template>

<script>
import headList from "../index/header/head_list.vue";
import mainList from "../index/nav/main_list.vue";

export default {
  components: { headList, mainList },
  data() {
    return {
      id: '',
      dataList: [],
      nav_main_list: [
        {
          url: "pages/page_lzj/inform/uninformedType/failureToGiveNotice",
          icon: "\ue61f",
          text: "未通知",
          color: "#ff4666",
          num: 0
        },
        {
          url: "pages/page_lzj/inform/shipments/shipments",
          icon: "\ue621",
          text: "已通知",
          color: "#6cc27c",
          num: ""
        },
        {
          url: 'pages/page_lzj/inform/failureMessage/failureMessage',
          icon: "\ue61c",
          text: "失败短信",
          color: "#ff4666",
          num: 0
        },
        {
            url: 'pages/page_lzj/index/messageHistory/messageHistory',
            icon: "\ue60a",
            text: "短信历史",
            color: "#0077e2",
            num: ""
        }
      ]
    };
  },
  methods: {
      headerEvent(){},
      init(){
          return new Promise(resolve => {
              this.fetch({
                  url: 'manager/order/selectcount',
                  body: {}
              }, (result) => {
                  let arr = [];
                  let res = result.res;

                  // 未通知数量与失败短信数量
                  this.nav_main_list[0].num = res.num;
                  this.nav_main_list[2].num = res.smssize;
                  // 今日入库与 总库存量
                  arr[0] = res.daycount;
                  arr[1] = res.repertory;
                  resolve(arr)
              })
          });
      }
  },
  async created() {
      // 页面显示时  dataList状态触发更新
      await this.onResume(this.init, 'dataList');
      // true 为代办点   false为快递员
      this.id = await this.verdictId();
  },
};
</script>

<style scoped>
  .tit{
    flex-direction: row;
    align-items: center;
    background-color: #0077e2;
    height: 100px;
    padding-left: 30px;
  }
  .titText{
    color: #fff;
    font-size: 28px;
  }
</style>
