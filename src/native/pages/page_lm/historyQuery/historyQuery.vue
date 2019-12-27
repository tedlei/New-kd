<template>
  <div style="flex:1">
    <headerNav :title="repertory" rightButton="" @headerEvent="headerEvent"></headerNav>
		<div class="hq-app" v-if="repertory!=='库存量'">
			<div class="hqDiv1" @click="topDiv(1)">
				<text :class="[hqSelNum===1?'fontColor':'']" class="hqFont">入库时间</text>
				<text style="font-family:iconfont;" :class="[hqSelNum===1?'fontColor':'']" class="divIcon">{{hqSelNum===1?'&#xe6bd;':'&#xe622;'}}</text>
			</div>
			<div class="hqDiv1 leftbd" @click="topDiv(2)">
				<text :class="[hqSelNum===2?'fontColor':'']" class="hqFont">快递状态</text>
				<text style="font-family:iconfont;" :class="[hqSelNum===2?'fontColor':'']" class="divIcon">{{hqSelNum===2?'&#xe6bd;':'&#xe622;'}}</text>
			</div>
			<div class="hqDiv1 leftbd" @click="topDiv(3)">
				<text :class="[hqSelNum===3?'fontColor':'']" class="hqFont">{{userRole===1?'代办点地址':'快递公司'}}</text>
				<text style="font-family:iconfont;" :class="[hqSelNum===3?'fontColor':'']" class="divIcon">{{hqSelNum===3?'&#xe6bd;':'&#xe622;'}}</text>
			</div>
		</div>

    <template v-if="hqSelNum&&repertory!=='库存量'">
      <selListCom v-if="hqSelNum===1" :selDate="selList[0]" :selNum="0" :ischickednum="ischicked1" @selcheck="selcheck"></selListCom>
      <selListCom v-if="hqSelNum===2" :selDate="selList[1]" :selNum="1" :ischickednum="ischicked2" @selcheck="selcheck"></selListCom>
      <selListCom v-if="hqSelNum===3" :selDate="selList[2]" :selNum="2" :ischickednum="ischicked3" @selcheck="selcheck"></selListCom>
    </template>

    <selTime v-if="isShowTime" @selymd="selymd"></selTime>
    
    <text class="quBtn" v-if="hqList.length===0&&repertory!=='库存量'" @click="queryCourier('1')">查询</text>
  
    <scroller class="qs" v-if="hqList.length>0" :loadmoreoffset="10" @loadmore="onLoadmore">

      <template >
        <courierList v-for="(e,i) of hqList" :key="i" :obj="e" :repertory="repertory" :boole="false"></courierList>
        <text :key="i">{{i}}</text>
      </template>

      <div class="loadingbox">
        <image class="loading" v-if="isShowimg===1" src="https://img.alicdn.com/tfs/TB1CWnby7yWBuNjy0FpXXassXXa-32-32.gif" />
        <text class="text_font" v-if="isShowimg===2">到底啦！！</text>
      </div>
      
    </scroller>
    <text></text>
  </div>
</template>

<script>
import hq from './hq'
export default {
  ...hq
};
</script>

<style scoped>
.hq-app {
  width: 750px;
  height: 88px;
  border-bottom-width: 2px;
  border-bottom-color: rgba(230, 230, 230, 1);
  border-bottom-style: solid;
  flex-direction: row;
}
.hqDiv1 {
  width: 249px;
  height: 88px;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.hqFont {
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.divIcon {
  margin-top: 3px;
  margin-left: 20px;
}
.leftbd {
  border-left-style: solid;
  border-left-color: rgba(232, 232, 232, 1);
  border-left-width: 2px;
}
.fontColor {
  color: rgba(0, 119, 226, 1);
}

.quBtn {
  margin-top:40px;
  margin-left: 30px;
  width: 690px;
  height: 88px;
  text-align: center;
  line-height: 88px;
  font-size: 40px;
  background-color: rgba(0, 119, 226, 1);
  color:white ;
}
.qs{
  flex: 1;
  margin-top:20px;
  height: 1050px;
}
.text_font{
  margin-top: 40px;
  text-align: center;
  font-size: 30px;
}
.loadingbox {
  padding-top: 20px;
  padding-bottom: 40px;
}
.loading{
  margin-left: 355px;
  height: 40px;
  width: 40px;
}
</style>
