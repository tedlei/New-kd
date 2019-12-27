<template>
   <div class="login-app">
        <headerNav title="注册" rightButton></headerNav>
      <div class="divTop">
        <div class="dtitle1" :class="[selNum===2?'borderStyle':'']" @click="clicksel(2)">
            <text class="text1" :class="[selNum===2?'textStyle':'']">驿站注册</text>
        </div>
        <div class="dtitle2" :class="[selNum===1?'borderStyle':'']" @click="clicksel(1)">
            <text class="text1" :class="[selNum===1?'textStyle':'']">快递员注册</text>
        </div>
      </div>
      <cous :selNum="selNum"></cous>
  </div>
</template>

<script>
import cous from './courierStation.vue'
export default {
    components: { cous},
    data () {
        return {
            selNum:2,  //选择注册方式
        }
    },
    methods: {
        clicksel:function(num){
            this.selNum=num;
        },
    }
};
</script>

<style scoped>
.divTop{
    margin-top:30px;
    width: 750px;
    flex-direction:row;
    justify-content: center;
}
.dtitle1,.dtitle2{
    height: 60px;
}
.text1{
    font-size:36px;
    color:rgba(51,51,51,1)
}
.dtitle2{
    margin-left:50px;
}
.borderStyle{
    border-bottom-style:solid;
    border-bottom-color:rgba(51,51,51,1);
    border-bottom-width:4px;
}
.textStyle{
    font-weight:600;
}
</style>