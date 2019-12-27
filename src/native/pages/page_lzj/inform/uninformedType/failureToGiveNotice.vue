<template>
    <slider class="slider" :style="{height: dh + 'px'}" interval="3000" :auto-play="false" :index="indexPage" :infinite="false">
      <div :style="{flex: 1}">
        <div class="content">
          <headerNav @headerEvent="headerEvent" title="未通知" right-button="设为不通知"></headerNav>
          <inputFrame @select="select" style="margin-top:20px;margin-bottom:20px;"></inputFrame>
        </div>
        <div style="flex: 1;">
          <scroller :style="{height: dh - 506 + 'px'}">
            <div class="list" v-for="(item, i) in listData" :key="i">
              <div class="list_top">
                <text style="color: #333333;fontSize: 30px;">{{item.company}}&nbsp;&nbsp;{{item.orderId}}</text>
                <text style="color: #999999;fontSize: 30px;">货号&nbsp;&nbsp;{{item.shelves}}</text>
              </div>
              <div class="list_bottom">
                <text style="color: #999999;fontSize: 30px;">电话&nbsp;&nbsp;{{item.expressOwnerTel}}</text>
              </div>
            </div>
          </scroller>
          <div class="list_last" :style="baseStyle.fCenter">
            <text :style="baseStyle.fs30" style="color: #999999;">共计: {{listData.length}}条</text>
          </div>
        </div>
        <div class="footer" :style="baseStyle.fCenter" @click="sendInform">
          <text style="color: #fff;font-size:44px;">发送通知</text>
          <text class="count">(共{{listData.length}}条)</text>
        </div>
      </div>
      <div style="position: absolute;">
        <setUtToNoNotice :listData="listData"
                         @update="update"
                         @headerEvent="headerEvent"></setUtToNoNotice>
      </div>
      <loadingNav ref="load"></loadingNav>
    </slider>
</template>

<script>
import inputFrame from "./input.vue";
import baseStyle from "../../../../common/baseStyle.js";
import setUtToNoNotice from './setUtToNoNotice.vue'
import findInformData from '../findInformData';
import sendSms from '../sendSms'
import backKeyEvent from '../../../../common/backKeyEvent'

export default {
  components: { inputFrame, setUtToNoNotice },
  data() {
    return {
      dh: 0,
      indexPage: 0,
      baseStyle,
      listData: []
    };
  },
  methods: {
    headerEvent(res) {
      this.indexPage = res === 'left' ? 0 : 1
    },
    async select(value){
        this.listData = await findInformData.find.call(this, value, 1);
        this.toast(this.listData.length ? '查询成功' : '未查找到数据');
    },
    // 当再设为不通知页面设置后更新当前页对应数据
    async update(){
      this.listData = await findInformData.init.call(this, 1);
    },
    sendInform(){
        let data = this.listData;
        //判断有没有短信
        if(!data.length){
          this.toast("没有可以重发的短信")
          return
        }
        sendSms.sendSms.call(this, "确认全部发送?", '2', data, result=>{
            // 发送成功
            this.toast('发送成功');
            this.update()
        });
    }
  },
  async created() {
    this.dh = this.getScreenHeight();
    // 当轮播不位于第一张时 执行切换 否则返回
    await backKeyEvent.androidEnableExit.call(this, (page)=>{
      if (this.indexPage === 1) {
        this.indexPage = 0
      } else this.pop()
    });
    this.listData = await findInformData.init.call(this);
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
  height: 116px;
  background-color: #fff;
}
.footer {
  width: 750px;
  height: 94px;
  background-color: rgba(0, 119, 226, 1);
}
.count {
  position: absolute;
  right: 182px;
  color: #fff;
  font-size: 28px;
}
.slider {
  flex: 1;
  width: 750px;
}
</style>
