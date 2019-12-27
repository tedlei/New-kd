<template>
  <div class="nav_main">
    <div
      class="nav_main_list"
      v-for="(item, index) in nav_main_list"
      :style="{width: optionSize || '187.5px'}" 
      :key="index"
      @click="toPage(item.url)"
    >
      <text v-if="item.num"
            class="num"
            :style="{
              lineHeight: textShow(item.num) ? '20px' : '36px',
              fontSize: textShow(item.num) ? '30px' : '24px'}"
            >{{textShow(item.num) ? '...' : item.num}}</text>
      <text class="iconfont nav_main_list_icon" :style="{color: item.color}">{{item.icon}}</text>
      <text :style="baseStyle.fs30" class="nav_main_list_text">{{item.text}}</text>
    </div>
  </div>
</template>

<script>
import baseStyle from "../../../../common/baseStyle.js";
export default {
  data() {
    return {
      baseStyle
    };
  },
  props: ["nav_main_list", "option-size"],
  methods: {
    toPage(url){
      if (url === 'pages/page_lm/inquire/inquireQueryChild'){
        // 查询
        // url = 'openSearchPackageActivity';
        this.push(url,{num:1});
        return ;
      } else if (url === 'pages/page_lm/historyQuery/historyQuery'){
          // 收件历史
          this.push(url,{from: '收件历史'});
          return ;
      } else if (url === 'pages/page_lm/quitPiece/quitPiece') {
        // 退件处理
        url = 'openOutPackageActivity';
      } else {
        this.push(url);
        return ;
      }
      this.getItem('userInfo', result => {
        let param = [result.data.sessionId];
        param.push(result.data.userRole*1);
        if (result.data.userRole === '1') {
          this.push('pages/page_lm/quitPiece/qpQueryChild');
        } else this.pushAndroid(url, param);
      });
    },
    // 获取显示数量是否过大
    textShow(value){
        return value.toString().length > 2;
    }
  }
};
</script>
<style scoped>
/* nav_main */
.iconfont{
    font-family: iconfont;
}
.nav_main{
    flex-direction: row;
    flex-wrap: wrap;
    padding-top: 12px;
}
.nav_main_list{
    justify-content: center;
    align-items: center;
    width: 187.5px;
    padding-top: 38px;
}
.nav_main_list_icon{
    padding-bottom: 20px;
    font-size: 60px;
}
.nav_main_list_text{
    color: #333333;
}
/* 数字相关 通知页--- */
.num {
  position: absolute;
  top:10px;
  right:40px;
  width: 36px;
  height: 36px;
  background-color: #ff0000;
  color: #fff;
  text-align: center;
  border-radius: 18px;
}
</style>
