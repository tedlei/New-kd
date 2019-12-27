<template>
  <div style="flex: 1;">
    <headerNav title="查看账单" rightButton></headerNav>
    <div class="content">
      <div class="content_title" :style="{backgroundColor: checked?'#fff':'#ccc'}" @click="cutChenked(true)">
        <text class="content_title_text1" :class="[checked?'checked':'']">充值记录</text>
        <text class="icon" :style="{transform: checked ? 'rotate(90deg)' : 'rotate(0deg)'}">&#xe622;</text>
      </div>
      <div class="content_title" :style="{backgroundColor: checked?'#ccc':'#fff'}" @click="cutChenked(false)">
        <text class="content_title_text1" :class="[checked?'':'checked']">消费记录</text>
        <text class="icon" :style="{transform: checked ? 'rotate(0deg)' : 'rotate(90deg)'}">&#xe622;</text>
      </div>
      <div class="content_date">
        <div class="shuru">
          <text style="color: #333;font-size: 34px">开始时间</text>
          <input :min="minDate()" :max="getCurrentDate()" class="input" type="date" v-model="beginDate" />
          <text class="icon" style="position:absolute;right:70px;top:24px;transform:rotate(90deg)">&#xe622;</text>
        </div>
        <div class="shuru" style="margin-top: 18px;">
          <text style="color: #333;font-size: 34px;">结束时间</text>
          <input :min="beginDate" :max="getCurrentDate()" class="input" type="date" v-model="endDate" />
          <text class="icon" style="position:absolute;right:70px;top:24px;transform:rotate(90deg)">&#xe622;</text>
        </div>
        <wxc-button
          :isHighlight="true" 
          :btnStyle="{backgroundColor: '#0077e2',marginTop: '40px'}" 
          text="查询" 
          @wxcButtonClicked="select"></wxc-button>
      </div>
    </div>
    <chongzhi v-if="checked" :list="chongzhiList" :allout="allout" :allrepertory="allrepertory"></chongzhi>
    <xiaofei v-else :list="xiaofeiList" :allout="allout" :allrepertory="allrepertory"></xiaofei>
  </div>
</template>

<script>
import { WxcButton } from 'weex-ui'
import chongzhi from './chongzhi.vue'
import xiaofei from './xiaofei.vue'

export default {
  components: {WxcButton, chongzhi, xiaofei},
  data() {
    return {
      checked: true, // 充值或消费
      beginDate: '',
      endDate: '',
      beginDate2: '',
      endDate2: '',
      chongzhiList: [],
      xiaofeiList: [],
    };
  },
  methods: {
    chuliString(str){
        return str = str.slice(0, 4) + str.slice(5, 7) + str.slice(8, 10);
    },
    getData(){
      // 起止时间
      let {checked, beginDate, endDate, beginDate2, endDate2} = this;
      // 获取数据
      let bd = null,
          ed = null,
          updataStateName = null;
      if (checked) {
          bd = beginDate;
          ed = endDate;
          updataStateName = 'chongzhiList'
      } else {
          bd = beginDate2;
          ed = endDate2;
          updataStateName = 'xiaofeiList'
      }
      bd = this.chuliString(beginDate);
      ed = this.chuliString(endDate);
      this.fetch({
          url: '/orderMoney/getmoneyrecord',
          body: {
              number: checked ? '1' : '2',
              starttime: bd,
              endtime: ed
          }
      }, result => {
          this[updataStateName] = result.res.data;
          this.allout = result.res.allout;
          this.allrepertory = result.res.allrepertory;
      });
    },
    // 切换充值消费页
    cutChenked(bool) {
      if (bool) {
        this.checked = true
      } else {
        this.checked = false
      }
      // 保存时间值， 下次切换回来时取出
      let bdate = this.beginDate,
          eDate = this.endDate;
      this.beginDate = this.beginDate2;
      this.endDate = this.endDate2;
      this.beginDate2 = bdate;
      this.endDate2 = eDate
    },
    // 获取当前时间
    getCurrentDate: function() {
      return JSON.stringify(new Date()).substring(1, 11);
    },
    // 时间最小值
    minDate(){
      return new Date(new Date() - 1000*60*60*24*180)
    },
    select(){
      let that = this;
      // 触发子组件获取数据方法
      that.getData()
    }
  },
  mounted(){
    this.beginDate = this.getCurrentDate();
    this.endDate = this.getCurrentDate();
    this.beginDate2 = this.getCurrentDate();
    this.endDate2 = this.getCurrentDate();
  }
};
</script>

<style scoped>
.content,
.content_title,
.shuru {
  flex-direction: row;
}
.content {
  flex-wrap: wrap;
}
.content_title {
  flex: 1;
  justify-content: center;
  align-items: center;
  height: 88px;
  border-width: 2px;
  border-style: solid;
  border-color: #f2f2f2;
}
.content_title_text1 {
  color: #666;
  font-size: 34px;
}
.icon {
  margin-left: 38px;
  color: #333;
  font-size: 22px;
  font-family: iconfont;
  transition: transform 0.3s;
}
.content_date{
  padding-top: 20px;
  padding-right: 28px;
  padding-bottom: 40px;
  padding-left: 30px;
  background-color: #fff;
}
.shuru{
  align-items: center;
}
.input {
  width: 540px;
  height: 72px;
  padding-left: 26px;
  margin-left: 20px;
  border-width: 2px;
  border-style: solid;
  border-color: #ccc;
  border-radius:10px;
}
.checked {
  color: #0077e2;
}
</style>
