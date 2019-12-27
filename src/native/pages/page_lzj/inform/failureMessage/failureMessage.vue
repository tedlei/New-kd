<template>
  <div :style="{flex:1}">
    <header-nav @headerEvent="headerEvent" title="失败短信" right-button />
    <inputFrame @select="select" style="margin-top:20px;margin-bottom:20px;" placeholder="请输入电话号码查询"></inputFrame>
    <div style="flex: 1;justify-content: space-between;">
      <scroller style="flex: 1;">
        <div class="list" v-for="(item, i) in listData"
          :key="i"
          @longpress="handleLongPress(i)"
          @click="cutStatus(i)"
          :mynum="i">
          <div class="list_top">
            <div style="flex-direction: row;">
              <text style="color: #999999;" :style="baseStyle.fs30">姓名&nbsp;&nbsp;</text>
              <text :style="baseStyle.fs30">{{item.nickName}}</text>
            </div>
            <div style="flex-direction: row;">
              <text style="margin-left:88px;color: #999999;" :style="baseStyle.fs30">电话&nbsp;&nbsp;</text>
              <text style="color:#333" :style="baseStyle.fs30">{{item.expressOwnerTel}}</text>
            </div>
            <!-- 失败短信 -->
            <text style="position:absolute;right: 8px;color: #FF0000;" :style="baseStyle.fs30">发送失败</text>
          </div>
          <div class="list_bottom">
            <text class="list_bottom_sub">{{item.smsContent.slice(0, 5)}}</text>
            <text class="list_bottom_text"
            >{{
              '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
              '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
              '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
              item.smsContent.slice(5)
              }}</text>
          </div>
          <!-- 复选按钮 -->
          <div v-if="openSelect" class="list_option" :class="[isArr[i] ? 'checked' : '']" :style="baseStyle.fCenter">
            <text style="color:#fff;font-size: 24px;font-family: iconfont;">&#xe614;</text>
          </div>
        </div>
      </scroller>
      <div v-if="!openSelect" class="footer" :style="baseStyle.fCenter" @click="sendAll">
        <text style="color: #fff;font-size:44px;">全部重新发送</text>
        <text class="count">(共{{listData.length}}条)</text>
      </div>
      <div v-else class="footer2">
        <div class="footer_left" @click="checkAll">
          <div class="list_option2" :class="[iscCheckAll ? 'checked' : '']" :style="baseStyle.fCenter">
            <text
              v-if="iscCheckAll"
              style="color:#fff;font-size: 24px;font-family: iconfont;"
            >&#xe614;</text>
          </div>
          <text :style="baseStyle.fs30">全选</text>
        </div>
        <text class="footer_right" @click="makeSure">重新发送</text>
      </div>
    </div>
    <loadingNav ref="load"></loadingNav>
  </div>
</template>

<script>
import inputFrame from "../uninformedType/input.vue";
import baseStyle from "../../../../common/baseStyle.js";
import select from '../select'
import sendSms from '../sendSms'

export default {
  components: { inputFrame },
  data() {
    return {
      dh: '',
      baseStyle,
      openSelect: false,  // 是否开启选择
      isArr: [], // 用于判断的数据
      iscCheckAll: false,
      listData: []
    };
  },
  methods: {
    headerEvent() {},
    // 全部重新发送
    sendAll() {
        //判断有没有短信
        let data = this.listData;
        if(!data.length){
          this.toast("没有可以重发的短信")
          return
        }

        sendSms.sendSms.call(this, "确认发送?", '1', data, ()=>{
            this.toast('发送成功');
            this.openSelect = false;
            // 更新本地数据
            this.isArr = [];
            this.listData = [];
        });
    },
    makeSure(){
        let isArr = this.stringify(this.isArr),
            listData = this.stringify(this.listData),
            deleteData = [];
        for (let i = isArr.length - 1; i > -1; i--) {
            // 选择则删除对应项
            if (isArr[i]) {
                // 获取删需除数据
                deleteData.push(listData[i]);
                isArr.splice(i, 1);
                listData.splice(i, 1);
            }
        }
        sendSms.sendSms.call(this, "确认发送?", '1', deleteData, ()=>{
            this.toast('发送成功');
            this.openSelect = false;
            // 更新本地数据
            this.isArr = isArr;
            this.listData = listData;
        });
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
    async select(value){
        if (!/^1[34578]\d{9}$/.test(value)) {
            this.toast('请输入正确的电话号码');
            return ;
        }
        this.fetch({
            url: 'orderSms/getsms',
            body: {
                expressownertel: value,
                smsstatus: 3
            }
        }, result => {
          let obj = result.res
          if(obj.code===200){
              if(!obj.data.length){
                this.toast("没有查询到数据！！！")
                return
              }
            this.listData = obj.data
            select.initData.call(this);

          }else{
            this.toast(obj.message)
          }
        })
    }
  },
  async created() {
    let data = await new Promise(resolve => {
        this.fetch({
            url: 'orderSms/getdefeatedsms',
            body: {}
        }, result => {
            resolve(result.res.data);
        });
    });
    this.listData = data;
    this.dh = this.getScreenHeight();
    // 生成用于判断的数据
    select.initData.call(this);
  }
};
</script>

<style scoped>
.list {
  height: 158px;
  padding: 20px 28px;
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
  flex-wrap: wrap;
  flex-direction: row;
  width: 694px;
}
.list_bottom_sub{
  position: absolute;
  top: 6px;
  left: -6px;
  font-size: 28px;
}
.list_bottom_text {
  width: 694px;
  color: #999999;
  font-size: 30px;
  line-height: 44px;
  lines: 2;
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
