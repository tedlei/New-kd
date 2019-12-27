<template>
  <div class="wrapper" :style="{flex:1}">
    <header-nav @headerEvent="headerEvent" title="客服" :right-button="openSelect ? '取消' : '删除'" />
    <scroller style="flex: 1;padding-bottom: 40px;padding-top: 20px;">
      <refresh class="refresh"
               style="padding-top: 30px;padding-bottom: 30px;"
               @refresh="onrefresh"
               @pullingdown="onpullingdown"
               :display="refreshing ? 'show' : 'hide'">
        <text class="indicator-text" style="color: skyblue;font-size: 28px;">loading ...</text>
        <loading-indicator class="indicator"></loading-indicator>
      </refresh>
      <div class="list" style="align-items: center;padding-top: 18px;" v-for="(item, i) in listData"
           :key="i"
           @longpress="handleLongPress(i)"
           @click="cutStatus(i)">
        <text v-if="item.showTimer" style="color: #999;padding-bottom: 18px;">{{item.timer}}</text>
        <div
                v-if="item.identity*1 === 0"
                style="flex-direction: row;width: 722px;"
                :style="{paddingLeft: openSelect ? '60px' : '20px'}">
          <image class="pic" src="root:img/hdb.png" alt=""></image>
          <div style="background-color: #0077E2;border-radius: 10px;">
            <text class="msgList_option_text">{{item.content}}</text>
          </div>
          <text class="msgList_option_icon2"
                :style="{left: openSelect ? '148px' : '110px', transform: 'rotate(180deg)'}">&#xe622;</text>
          <!-- 复选按钮 -->
          <div
                  v-if="openSelect"
                  class="list_option list_option_left"
                  :class="[isArr[i] ? 'checked' : '']"
                  :style="baseStyle.fCenter">
            <text style="color:#fff;font-size: 24px;font-family: iconfont;">&#xe614;</text>
          </div>
        </div>
        <div v-if="item.identity*1 === 1" style="flex-direction: row-reverse;;width: 722px;"
             :style="{paddingLeft: '20px'}">
          <image class="pic" style="margin-left: 20px;" src="root:img/hdb.png" alt=""></image>
          <div style="background-color: #0077E2;border-radius: 10px;">
            <text class="msgList_option_text">{{item.content}}</text>
          </div>
          <text class="msgList_option_icon2" :style="{right: '110px'}">&#xe622;</text>
          <!-- 复选按钮 -->
          <div
                  v-if="openSelect"
                  class="list_option list_option_left"
                  :class="[isArr[i] ? 'checked' : '']"
                  :style="baseStyle.fCenter">
            <text style="color:#fff;font-size: 24px;font-family: iconfont;">&#xe614;</text>
          </div>
        </div>
        <div :ref="'indicator-' + i" class="indicator"></div>
      </div>
    </scroller>
    <wxc-searchbar
            v-if="!openSelect"
            ref="input"
            placeholder="请输入内容"
            cancel-label="发送"
            @focus="focus"
            @blur="blur"
            :always-show-cancel="sendBtn"
            @wxcSearchbarCancelClicked="wxcSearchbarCancelClicked"></wxc-searchbar>
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
  import { WxcSearchbar } from 'weex-ui'
  const dom = weex.requireModule('dom');

  export default {
    components: {WxcSearchbar},
    data() {
      return {
        ws: '',
        canType:false,  // websocket是否连接成功
        refreshing: false,  // 是否隐藏下拉加载
        sendBtn: false, // 输入框发送按钮是否显示
        baseStyle,
        openSelect: false,
        isArr: [], // 用于判断的数据
        iscCheckAll: false,
        listData: [],
        userPhone: ''  // 用于存储用户数据
      };
    },
    methods: {
      // 下拉完成时触发  // 我在这边运行阔以赛 1 不用再运行androidl额 因为我是直接指向的这边的js
      onrefresh (event) {
        this.refreshing = true;
        setTimeout(() => {
          this.refreshing = false
        }, 2000)
      },
      // 下拉时触发
      onpullingdown (event) {
        // 触发上啦加载函数
      },
      // 点击发送按钮触发
      wxcSearchbarCancelClicked(event){
        if (!this.canType) {
          this.toast('无法连接');
          return ;
        }
        // this.$refs.input.autoBlur()
        // 发送给后台的数据
        let userPhone = this.userPhone;
        let data = (function (){
          return event.value + '[|]' + userPhone
        })();

        this.ws.send(data);
        // 更新本地数据 因为不需要存储因此只需要更新本地即可
        this.sendMsg(event.value, 1);
      },
      // input的获取焦点和失去焦点
      focus(){this.sendBtn = true;},
      blur(){this.sendBtn = false},
      // 发送信息
      sendMsg(msg, num){
        let source = this.stringify(this.listData),
            len = source.length;
        let obj = '';
        if (typeof msg !== 'object'){
          let date = this.timestamp();
          obj = {
            id: len + 1,
            timer: date,
            content: msg,
            identity: num,
            showTimer: true
          }
        } else {
          msg.id = len + 1;
          obj = msg;
        }

        // 当前信息的时间是否显示  时间差小于5分钟则不显示
        if (len !== 0 && (this.getUTCTimestamp(obj.timer) - this.getUTCTimestamp(source[len-1].timer)) < 1000*60*5){
          obj.showTimer = false
        }

        let length = this.listData.push(obj);
        this.isArr.push(false);
        // 清空输入框
        this.$refs.input.setValue('');
        let timer = setTimeout(()=>{
          this.go2bottom(length);
          // 更新本地存储的聊天对象
          this.storage.removeItem('chatMessage');
          this.setItem('chatMessage', this.listData);
          clearInterval(timer);
        }, 200)
      },
      getUTCTimestamp(date){
        date = date.split(' ');
        // 年月日数组集合
        let ymd = date[0].split('-');

        // 根据时间字符串获取时间
        let dateString = new Date('Wed Aug ' + ymd[2] + ' ' + ymd[0] + ' ' + date[1] + ' GMT+0800 (CST)');
        // 更改月份
        dateString.setMonth(ymd[1] - 1);
        return dateString;
      },
      // 发送消息后始终保持显示到最底部
      go2bottom: function(length) {
        let indicatorId = 'indicator-' + (length-1),
                indicator = this.$refs[indicatorId];
        // 滚动到上一条消息的底部
        if (!indicator) return;
        dom.scrollToElement(indicator[0], {});
      },
      /** start websocket 相关 */
      open(){
        // 做一个延时，以免建连太快而抖动
        let timer = setTimeout(()=>{
          this.canType = true;
        }, 300)
      },
      // 监听服务器发送来的数据
      message(msg){
        msg = JSON.parse(msg.data);
        msg.timer = this.timestamp();
        msg.showTimer = true;
        // 延迟响应
        let timer = setTimeout(()=>{
          this.sendMsg(msg, 0);
          clearInterval(timer);
        }, 300);
      },
      error(){},
      close(){this.ws.close()},
      /** end websocket 相关 */

      /** start 选择删除 相关 */
      // 头部点击为右边删除按钮时  openSelect是否开启选择删除
      headerEvent(val){
        if (val !== 'right') return;
        this.openSelect = !this.openSelect
      },
      // 确认弹窗
      confirm(){
        this.modal.confirm({
          message: '确认删除',
          duration: 1,
          okTitle: '确认',
          cancelTitle: '取消'
        }, value => {
          if (value === '取消') return;
          let arr = this.stringify(this.isArr),
              listData = this.listData;
          // 是否至少选中一个选项
          if (arr.some((item)=>{
            return item;
          })) {
            // 关闭选择
            this.openSelect = false;
            for (let len = arr.length - 1, i = len; i > -1; i--) {
              if (arr[i]) {
                arr.splice(i, 1);
                listData.splice(i, 1);
              }
            }

            this.isArr = arr;
            this.listData = listData;
            // 更新本地存储的聊天对象
            this.storage.removeItem('chatMessage');
            this.setItem('chatMessage', listData);
            let page=weex.requireModule("page");
            page.reload();
          } else this.toast('请选择删除项');
        });
      },
      // 长按开启选择
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
      // 删除所有
      deleteAll(){this.confirm()},
      /** end 选择删除 相关 */
      init(){
        return new Promise(resolve=>{
          this.getItem('chatMessage', (res)=>{
            if (res.result === 'failed' || res.data.length === 1) {
              let date = this.timestamp(),
                      obj = [
                        {
                          id: 1,
                          timer: date,
                          content: '请问您有什么问题？',
                          identity: 0,
                          showTimer: true
                        }
                      ];
              // 全局写入一次聊天信息
              this.setItem('chatMessage', obj);
              resolve(obj);
            } else resolve(res.data);
          })
        });
      }
    },
    async created() {
      // 将一次性存储的聊天信息赋给渲染数据
      this.listData =   await this.init();

      // 初始化判断数据
      select.initData.call(this)
    },
    mounted(){
      // 获取用户手机号
      this.getItem('userInfo', (res)=>{
        let userPhone = res.data.userPhone;
        this.userPhone = userPhone;

        // websocket连接
        const ws = weex.requireModule('webSocket');
        ws.WebSocket('ws://39.100.11.220:8080/webSocket/' + userPhone,'');
        const {open, message, error} = this;
        ws.onopen.call(this, open);
        ws.onmessage.call(this,message);
        ws.onerror.call(this,error);
        this.ws = ws;
      });

      // 滚动到底部
      let timer = setTimeout(()=>{
        this.go2bottom(this.listData.length);
        clearInterval(timer);
      }, 200);
    }
  };
</script>

<style scoped src="../../mine/systemMessages/systemMessages.css"></style>
<style scoped>
  .list{
    padding-left: 0;
  }
  .list_option_left{
    left: 0;
  }
  .msgList_option_icon2{
    position: absolute;
    top: 4px;
    color: #0077e2;
    font-size: 28px;
    font-family: iconfont;
  }

  /* 上拉加载更多 */
  .refresh {
    width: 750px;
    display: -ms-flex;
    display: -webkit-flex;
    display: flex;
    -ms-flex-align: center;
    -webkit-align-items: center;
    -webkit-box-align: center;
    align-items: center;
  }
  /* 加载下相关样式  */
  .loading-wrapper {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    justify-content: center;
    align-items: center;
  }
  .loading-box {
    justify-content: center;
    width: 400px;
    height: 130px;
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 10px;
  }
  .tip {
    text-align: center;
    color: #fff;
  }
</style>
