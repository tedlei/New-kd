<template>
  <div :style="{flex:1}">
    <header-nav @headerEvent="headerEvent" title="系统消息" right-button="删除" />
    <scroller style="flex: 1;">
      <div class="list" style="align-items: center;" v-for="(item, i) in listData"
        :key="i"
        @longpress="handleLongPress(i)"
        @click="cutStatus(i)"
        :mynum="i">
        <text v-if="item.timer" style="padding-bottom: 18px;color: #999;">{{item.timer}}</text>
        <div style="flex-direction: row;width: 722px;">
          <image class="pic" src="root:img/hdb.png" alt=""></image>
          <div style="background-color: #0077E2;border-radius: 10px;">
            <text class="msgList_option_text">{{item.content}}</text>
          </div>
          <text class="msgList_option_icon">&#xe622;</text>
          <!-- 复选按钮 -->
          <div
            v-if="openSelect"
            class="list_option"
            :class="[isArr[i] ? 'checked' : '']"
            :style="baseStyle.fCenter">
            <text style="color:#fff;font-size: 24px;font-family: iconfont;">&#xe614;</text>
          </div>
        </div>
      </div>
    </scroller>
    <div v-if="openSelect" class="footer2">
      <div class="footer_left" @click="checkAll">
        <div class="list_option2" :class="[iscCheckAll ? 'checked' : '']" :style="baseStyle.fCenter">
          <text
            v-if="iscCheckAll"
            style="color:#fff;font-size: 24px;font-family: iconfont;"
          >&#xe614;</text>
        </div>
        <text :style="baseStyle.fs30">全选</text>
      </div>
      <text class="footer_right" @click="deleteAll">删除</text>
    </div>
  </div>
</template>

<script>
import baseStyle from "../../../../common/baseStyle.js";
import select from '../../inform/select'
// const ws = weex.requireModule('webSocket');

export default {
  data() {
    return {
      baseStyle,
      openSelect: false,
      isArr: [], // 用于判断的数据
      iscCheckAll: false,
      listData: [
        {
          id: 1,
          timer: '2019年7月18日',
          content: '您的余额不足1元，为了不影响您的使用，请您立即充值。'
        },
        {
          id: 2,
          timer: '2019年7月18日',
          content: '您今天充值了1元，实际到账1元，账户余额1元。'
        },
        {
          id: 3,
          timer: '2019年7月18日',
          content: '双11大反馈，充50送50啦！'
        },
        {
          id: 4,
          timer: '2019年7月18日',
          content: '软件已经升级，立即更新'
        }
      ]
    };
  },
  methods: {
    headerEvent(val){
      if (val !== 'right') return;
      this.confirm()
    },
    confirm(){
      this.modal.confirm({
        message: '确认删除',
        duration: 1,
        okTitle: '确认',
        cancelTitle: '取消'
      }, (value) => {
        if (value === '取消') return;
        // 请求删除接口  更新当前数据 listData
        this.openSelect = false;
        // 重新初始化数据
        select.initData.call(this)
      })
    },
    // 长按选择
    handleLongPress(num) {
       // 开启选择
      if (this.openSelect) return;
      this.openSelect = true;
      this.cutStatus(num)
    },
    // 全选按钮
    checkAll() {
      select.checkAll.call(this)
    },
    // 单个切换
    cutStatus(i) {
      select.cutStatus.call(this, i)
    },
    deleteAll(){this.confirm()}
  },
  created() {
    // 生成用于判断的数据
    select.initData.call(this);
    // // socket
    // let self = this;
    // ws.WebSocket('ws://192.168.3.7:9090');
    //
    // ws.onopen = function(e) {
    //   // 做一个延时，以免建连太快而抖动
    //   let timer = setTimeout(function() {
    //     // self.canType = true;
    //     clearInterval(timer)
    //   }, 300);
    // };
    // ws.onmessage = function (e){
    //
    // };
    // ws.onerror = function(e) {
    //   this.toast('网络错误，请稍后重试。');
    // };
    //
    // ws.send('nihao')

    // ws.WebSocket('ws://192.168.3.7:8080/webSocket/username=dadsad','');
    // ws.onopen = (event) => {
    // }
    // ws.send('41465465')
  },
  mounted(){
    select.initData.call(this)
  }
};
</script>

<style src="./systemMessages.css" scoped></style>
