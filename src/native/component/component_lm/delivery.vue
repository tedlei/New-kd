<template>
  <div class="delivery-app bd">
    <ul class="daul">
      <li class="dali"><text class="textColor1">快递公司：{{company}}</text></li>
      <li class="dali"><text class="textColor1">单号：{{waybillNUm}}</text></li>
    </ul>
    <ul class="daul mt20">
      <li class="dali"><text class="textColor1">姓名：{{uName}}</text></li>
      <li class="dali"><text class="textColor1">手机号：{{phone}}</text></li>
    </ul>
    <ul class="daul mt20">
      <li class="dali">
        <text class="textColor1">状态：{{deliveryState?'已':'未'}}出库 &nbsp;&nbsp;&nbsp;在库{{timeNum}}天</text>
      </li>
      <li class="dali"><text class="textColor1">货号：{{cargoNum}}</text></li>
    </ul>
    <wxc-button text="确认出库" class="btn bd" type="big" :textStyle="btnStyle"></wxc-button>
    
  </div>
</template>

<script>
import { WxcButton } from 'weex-ui'
export default {
  components: { WxcButton },
  data () {
    return {
      company:'申通',   //快递公司
      waybillNUm:'12345678912345',    //运单号
      uName:'赵老板',   //姓名
      phone: "15823961672", //电话
      deliveryState:false,    //是否出库
      timeNum:1,     //在库天数
      cargoNum:'0715001',  //货号 


      btnStyle:{    //button 样式
        color:'rgba(51,51,51,1)',
        fontSize: '28px'
      }
    };
  },
  created () {
    
  }
};
</script>

<style scoped>
.bd{
  border-style:solid;
  border-width: 1px;
}
.delivery-app{
  width: 690px;
  height:270px;
  margin-left: 30px;
  margin-top:20px;
  padding-left:30px;
  padding-right:20px;
  border-color:rgba(204,204,204,1);
  border-radius:10px;
}
.daul{
  margin-top:20px;
  flex-direction: row;
}

.dali{
  width: 325px;
}
.textColor1{
  font-size:28px;
  color:rgba(153,153,153,1);
}
.btn{
  margin-top:30px;
  margin-left: 236px;
  width:160px;
  height:60px;
  border-radius:5px;
}
</style>
