<template>
  <ul>
    <li class="slli" v-for="(e,i) in selDate" :key="i" @click="topChecked(i)">
        <div style="flex:1;" class="slFont">
          <text :class="[ischickednum === i?'fontC':'']" class="font">{{e.title===''?'全部':e.title}}</text>
        </div>
        <div class="slIcon" :class="[ischickednum === i?'bgColor':'']" :style="bs.borderWS">
          <text :class="[ischickednum === i?'fontColor':'']" style="font-family:iconfont;">&#xe614;</text>
        </div>
    </li>
  </ul>
</template>

<script>
import { WxcIcon } from "weex-ui";
import bs from "../../../common/baseStyle.js";
export default {
  components: { WxcIcon },
  props:['selDate','selNum','ischickednum'],
  data() {
    return {
      bs,
      topNum:0,
    };
  },
  methods:{
    topChecked:function(num){
      this.topNum = num;
      this.$emit('selcheck',this.selNum,num)
    },
  },
  watch:{
    'selNum':function(){
      this.topNum = 0;
    },
  }
};
</script>

<style scoped>
.slli{
  height:100px;
  padding-left: 30px;
  padding-right: 30px;
  border-bottom-color:rgba(230,230,230,1);
  border-bottom-style:solid;
  border-bottom-width:2px;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.slFont{
  padding-right: 30px;
}
.font{
  font-size: 34px;
  color:rgba(153,153,153,1);
}
.slIcon{
  width: 40px;
  height: 40px;
  border-color: rgba(153,153,153,1);
  border-radius:50%;
  justify-content: center;
  align-items: center;
}
.fontColor{
  color:white;
}
.fontC{
  color:rgba(0,119,226,1);
}
.bgColor{
  background-color:rgba(0,119,226,1);
  border-color: rgba(0,119,226,1);
}
</style>
