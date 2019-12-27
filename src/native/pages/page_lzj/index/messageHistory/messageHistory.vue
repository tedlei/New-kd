<template>
  <div :style="{flex:1}">
    <header-nav @headerEvent="headerEvent" title="短信历史" right-button />
    <inputFrame type="number"
                @getInput="getInput"
                @select="select"
                placeholder="请输入电话号码查询"
                style="margin-top:20px;margin-bottom:20px;"></inputFrame>
    <div style="flex: 1;justify-content: space-between;">
      <scroller style="flex: 1;">
        <div class="list" v-for="(item, i) in listData"
          :key="i"
          :mynum="i"
          @click="smsList(item.expressOwnerTel)">
          <div class="list_top">
            <div style="flex-direction: row;">
              <text style="color: #333333;" :style="baseStyle.fs30">{{item.expressOwnerTel}}</text>
            </div>
            <text style="position:absolute;right: 8px;font-size: 30px;color: #999999;"
            >{{item.smsTime.substr(5, 2) + '月' + item.smsTime.substr(8, 2) + '日'}}</text>
          </div>
          <div class="list_bottom">
            <text style="font-size: 30px;">{{item.smsContent.substr(0, 7)}}</text>
            <text class="list_bottom_text">{{item.smsContent.substr(8, item.smsContent.length - 1)}}</text>
          </div>
        </div>
      </scroller>
    </div>
  </div>
</template>

<script>
import inputFrame from "../../inform/uninformedType/input.vue";
import baseStyle from "../../../../common/baseStyle.js";

export default {
  components: { inputFrame },
  data() {
    return {
      baseStyle,
      openSelect: false,
      isArr: [], // 用于判断的数据
      iscCheckAll: false,
      listData: [],  // 渲染数据
      expressownertel: '',
      oldExpressownertel: '',
      smsContentTitle: ''
    };
  },
  methods: {
    headerEvent() {},
    toastHint(msg){
      this.toast(msg)
    },
    getInput(value){
      this.expressownertel = value
    },
    select(){
      if (this.oldExpressownertel === this.expressownertel){
        this.toast('查询成功');
        return
      }
      let expressownertel = this.expressownertel;
      if (!/^1[34578]\d{9}$/.test(expressownertel)) {
        this.toast('请输入正确的电话号码');
        return
      }
      this.fetch({
        url: 'orderSms/getsms',
        body: {
          expressownertel
        }
      }, (result)=>{
        let msg = '查询成功';
        let data = result.res.data;
        this.listData = data;
        if (!data.length) msg = '未查找到数据';
        else this.oldExpressownertel = data[0].expressOwnerTel;
        this.toast(msg)
      })
    },
    // 跳转到短信列表
    smsList(phone){
      this.push('pages/page_lzj/index/messageHistory/messageHistoryDetail', {phone});
    }
  },
  created() {
    this.fetch({
      url: 'orderSms/getListSms ',
      body: {}
    }, (result)=>{
      this.listData = result.res.data;
    })
  }
};
</script>

<style scoped>
.list {
  height: 158px;
  padding: 30px 28px 0;
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
  width: 694px;
}
.list_bottom {
  padding-top: 18px;
}

.list_bottom_text{
  width: 520px;
  color: #999;
  font-size: 30px;
  text-indent: -20px;
  lines: 1;
  text-overflow: ellipsis;
}
.footer {
  width: 750px;
  height: 124px;
  background-color: rgba(0, 119, 226, 1);
  opacity: 1;
}
.count {
  position: absolute;
  right: 136px;
  color: #fff;
  font-size: 28px;
}
.list_option,
.list_option2{
  width: 40px;
  height: 40px;
  margin-left: 30px;
  margin-right: 20px;
  border-radius: 20px;
  border-width: 2px;
  border-style: solid;
  border-color: rgba(102, 102, 102, 1);
}
/* 按钮 */
.list_option {
  position: absolute;
  top: 90px;
  right: 70px;
}
.checked {
  border-color: transparent;
  background-color: #0077e2;
}

/*  选中后底部 */
.footer2 {
  flex-direction: row;
  width: 750px;
  height: 124px;
}
.footer_left,
.footer_right {
  flex: 1;
  opacity: 1;
}
.footer_left {
  flex-direction: row;
  align-items: center;
  border-top-width: 2px;
  border-top-style: solid;
  border-top-color: rgba(204, 204, 204, 1);
}
.footer_right {
  height: 124px;
  background-color: #0077e2;
  color: #fff;
  font-size: 44px;
  text-align:center;
  line-height: 124px;
}
.footer:active{
  opacity: .5;
}
.footer_left:active{
  opacity: .5;
}
.footer_right:active{
  opacity: .5;
}
</style>
