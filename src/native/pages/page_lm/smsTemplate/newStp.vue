<template>
<!-- 不需要的页面 -->
  <div class="st-app">
    <headerNav title="新增短信模板"  rightButton=""></headerNav>
    <div class="new_div">
      <text class="new_text_style colorBlue" >【公司】</text>
      <text class="new_text_style">快递，您的快递已放在</text>
      <text class="new_text_style colorBlue">【地址】</text>
      <text class="new_text_style">请及时去取件,</text>
      <text class="new_text_style" :style="{marginTop:'20px',marginLeft:'14px'}">不便领取请联系</text>
      <text class="new_text_style colorBlue" :style="{marginTop:'20px'}">【电话】。【自定义】</text>
    </div>
    <div class="new_div_inp">
      <text class="new_inp_text">公司</text>
      <input type="text" placeholder="请输入公司" v-model="gs" class="new_inp"></input>
    </div>
    <div class="new_div_inp">
      <text class="new_inp_text">电话</text>
      <input type="text" placeholder="请输入电话" v-model="phone" class="new_inp"></input>
    </div>
    <div class="new_div_inp">
      <text class="new_inp_text">地址</text>
      <input type="text" placeholder="请输入地址"  v-model="dz" class="new_inp"></input>
    </div>
    <div class="new_div_inp">
      <text class="new_inp_text">自定义</text>
      <input type="text" placeholder="请输入自定义类容"  v-model="zdy" class="new_inp ml"></input>
    </div>
    <!-- <text class="sy">剩余可输入{{countNum}}字</text> -->
    <text class="stBtn" @click="commit">添加</text>
  </div>
</template>

<script>
export default {
  data() {
    return {
      gs:'',
      phone:'',
      dz:'',
      zdy:'',
      selNum:0,
    };
    
  },
  async created(){
    let value = await this.getParam();
    this.selNum = value.selNum*1-1;
  },
  methods:{
    commit:function(){
      let str = '';
      if(!this.gs.trim()){
        this.toast("请输入公司！！");
        return;
      }
      if(!this.phone.trim()){
        this.toast("请输入电话！！");
        return;
      }
      if(!this.dz.trim()){
        this.toast("请输入地址！！");
        return;
      }
      str = ''+this.gs+'快递，您的快递已放在'+this.dz+'，请及时去取件,不便领取请联系'+this.phone+'。'+this.zdy
      this.getItem('smsList',res=>{
        let obj = res.data
        obj[this.selNum].push({smsTitle:'自定义'+obj[this.selNum].length,smsStr:str})
        this.setItem('smsList',obj);
        this.toast('添加成功！')
        this.pop();
      });
    },
  },
  // computed:{
  //   countNum:function(){
  //     return 25-this.zdy.length;
  //   }
  // }
};
</script>

<style scoped>
.st-app{
  width: 750px;
  flex: 1;
}
.new_div{
  padding-top:30px;
  padding-bottom: 30px;
  flex-direction: row;
  flex-wrap: wrap;
  border-bottom-style:solid;
  border-bottom-width: 1px;
  border-bottom-color: rgba(230,230,230,1);
}
.new_text_style{
  color:#666666;
  font-size: 30px;
}
.colorBlue{
  color:#0077E2;
}
.new_div_inp{
  margin-left: 30px;
  padding-top: 10px;
  padding-bottom: 10px;
  width: 690px;
  flex-direction: row;
  align-items: center;
  border-bottom-style:solid;
  border-bottom-width: 1px;
  border-bottom-color: rgba(230,230,230,1);
}
.new_inp_text{
  margin-left: 30px;
  font-size: 28px;
  color:rgba(102,102,102,1);
}
.new_inp{
  margin-left: 90px;
  flex: 1;
  height: 60px;
  font-size: 28px;
}
.ml{
  margin-left: 63px;
}
.sy{
  margin-top:20px;
  padding-right: 28px;
  text-align: right;
  font-size: 28px;
  color:rgba(102,102,102,1);
}
.stBtn{
  margin-top: 60px;
  margin-left: 30px;
  width: 690px;
  height: 88px;
  background-color:rgba(0,119,226,1) ;
  border-radius:10px;
  text-align: center;
  line-height: 88px;
  color: white;
  font-size: 44px;
  color:rgba(255,255,255,1);
}

</style>
