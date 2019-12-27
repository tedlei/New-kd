<template>
  <div class="qg-app" :style="bs.borderWS">
    <div class="qgul">
        <text class="qgFont">{{expressage.company}} &nbsp;&nbsp;&nbsp; {{expressage.waybillNUm}}</text>
    </div>
    <ul class="qgul">
        <li class="qgli"><text class="qgFont">姓名：{{expressage.uName}}</text></li>
        <li class="qgli ml"><text class="qgFont">手机号：{{expressage.phone}}</text></li>
    </ul>
    <ul class="qgul">
        <li class="qgli">
            <text class="qgFont">状态：{{expressage.deliveryState===1?'未':expressage.deliveryState===2?'取件':'退件'}}出库</text>
        </li>
        <li class="qgli ml"><text class="qgFont">货号：{{expressage.waybillNUm}}</text></li>
    </ul>
    <ul class="qgul">
        <li class="qgli"><text class="qgFont">快递员：{{expressage.courierName}}</text></li>
        <li class="qgli ml"><text class="qgFont">电话：{{expressage.courierPhone}}</text></li>
    </ul>
    <text class="btn" v-if="expressage.inform===1" :style="bs.borderWS" @click="topInform">通知快递员</text>
    <text class="btn" v-if="expressage.inform===2&&expressage.deliveryState===1" :style="bs.borderWS" @click="topQuit">退件出库</text>
    <text class="btn" v-if="expressage.inform===2&&expressage.deliveryState===3" :style="bs.borderWS" @click="topAnnul">撤销退件出库</text>
  </div>
</template>

<script>
import bs from '@/common/baseStyle.js';
const modal = weex.requireModule('modal');
export default {
  data() {
    return {
        bs,
        expressage:{
            company: "", //公司
            waybillNUm: "", //运单号
            cargoNum: "", //货号
            deliveryState: 1, //是否出库（1：未出库，2：取件出库，3：退件出库）
            phone: "", //电话
            inform: 1,    //通知快递员（1：未通知，2:已通知）
            uName: '',   //姓名
            enterTime: '',   //入库时间
            endTime: '',   //入库时间
            timeNum: 0,      //入库天数
            courierName:'',  //快递员姓名
            courierPhone:'',  //快递员电话
        },
    };
  },
  mounted(){

  },
  methods:{
    //提示消息
    ts: function (e1, e2, e3) {
        console.log(e1, e2, e3);
        modal.toast({ 'message': e1 + " " + e2 + " " + e3, 'duration': '' });
    },

    //点击通知快递员
    topInform:function(){
        this.expressage.inform = 2;

    },

    //点击退件出库
    topQuit:function(){
        this.expressage.deliveryState = 3;

    },

    //点击退件出库
    topAnnul:function(){
        this.expressage.deliveryState = 1;
        this.expressage.inform = 1;
        
    },
  }
};
</script>

<style scoped>
.qg-app{
    margin-top:30px;
    margin-left: 30px;
    width: 690px;
    border-color:rgba(204,204,204,1);
    border-radius:10px;
}
.qgul{
    margin-top:20px;
    margin-left: 20px;
    flex-direction: row;
}
.qgli{
    width: 330px;
}
.qgFont{
    font-size:28px;
    color:rgba(153,153,153,1);
}
.ml{
    margin-left: 30px;
}
.btn{
    margin-left: 206px;
    margin-top: 22px;
    margin-bottom:20px;
    width: 280px;
    height: 60px;
    text-align: center;
    line-height: 60px;
    border-color:rgba(102,102,102,1);
    border-radius:10px;
    font-size: 28px;
    color:rgba(51,51,51,1);
}
</style>
