<template>
  <div class="rete-app">
      <headerNav title = '滞留件' rightButton=""></headerNav>
    <div class="divBtn"> 
      <text class="rfont" :class="[retStartNum==='1'?'rbgColor':'']" @click="topInform('1')">未通知</text>
      <text class="rfont" :class="[retStartNum==='2'?'rbgColor':'']" @click="topInform('2')">已通知</text>
    </div>

    <scroller class="scro" v-if="informList.length>0"> 
      <retentionList v-for="(e,i) of informList" :key="i" @getInformList="getInformList" :startNum="retStartNum" :eobj = "e"></retentionList>
      <!-- <div class="loadingbox">
        <image class="loading" v-if="isShowimg" src="https://img.alicdn.com/tfs/TB1CWnby7yWBuNjy0FpXXassXXa-32-32.gif">
        <text class="text_font" v-else>没有更多数据！！</text>
      </div> -->
    </scroller>
    <text class="text_font" v-else>没有更多数据！！</text>
    
    <!-- <text>{{JSON.stringify(informList)}}</text> -->
  </div>
</template>

<script>
import retentionList from './retentionList.vue'
export default {
  components:{retentionList},
  data() {
    return {
      retStartNum:'1',   //通知状态(1:未通知   2：已通知)
      informList:[],     //滞留件列表
      isShowimg:true,   //是否显示加载图标
    };
  },
  mounted(){
    this.getInformList(this.retStartNum,1)
    
  },
  methods:{
    //通知状态切换
    topInform:function(num){
      if(this.retStartNum===num) return;
      this.retStartNum = num;
      this.informList = [];
      this.getInformList(num,1)
    },

    //获取后台数据
    getInformList:function(num,number){
      if(number===0){
        this.informList = [];
        number = 1;
      }
      let url = 'manager/order/retention';
      let body = {smsstatus:num,index:number+''};
      this.fetch({ url ,body },res=>{
        let obj = res.res;
        if(obj.code===200){
          let arr = obj.data;
          if(arr.length>0){
            this.circulation(obj.data)
          }else{
            this.toast('没有更多数据！！')
            this.isShowimg = false
          }
        }else if(obj.code===401){
          this.toast(obj.message)
        }
      },err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        })
    },

    // 获取对象的值
    circulation:function(obj){
      for(let o in obj){
        let dx = {};
        dx.company = obj[o].company;
        dx.waybillNUm = obj[o].orderId;
        dx.cargoNum = obj[o].shelves;
        dx.deliveryState = obj[o].orderStatus;   //出库状态
        dx.phone = obj[o].expressOwnerTel;
        dx.uName = obj[o].nickName;
        dx.enterTime = obj[o].handleTime;
        dx.endTime = obj[o].ckTime;
        dx.timeNum = obj[o].day;
        dx.inform = obj[o].smsStatus;     //客户短信通知状态
        dx.courierPhone = obj[o].userId;
        dx.courierName = obj[o].userName;
        dx.courierLocaltion = obj[o].agentAddress
        this.informList.push(dx);
      }
    },

    onloadmore:function(){
      let index = Math.ceil(this.informList.length/10)+1;
      this.isShowimg = true
      this.getInformList(this.retStartNum,index)
    }
  },
};
</script>

<style scoped>
.rete-app{
  flex: 1
}
.divBtn{
  width: 750px;
  height: 88px;
  border-bottom-style: solid;
  border-bottom-width: 2px;
  border-bottom-color: rgba(204,204,204,1);
  flex-direction: row;
}
.rfont{
  width: 375px;
  height: 87px;
  line-height: 87px;
  text-align: center;
  background-color:rgba(204,204,204,1);
  font-size:34px;
  color:rgba(51,51,51,1);
}
.rbgColor{
  background-color: white;
  color:rgba(0,119,226,1);
}
.scro{
  flex: 1;
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
