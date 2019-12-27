<template>
   <div>
       <headerNav title="联系我们" rightButton></headerNav>
       <div class="content">
           <text class="content_text">公司邮箱：huiduobao@touch-ez.com.cn</text>
           <div style="flex-direction: row;align-items: flex-end;">
               <text class="content_text" style="margin-top: 30px;">客服电话：</text>
               <text @click="tel" style="color: #148fee;font-size: 28px;">15823961672</text>
           </div>
       </div>
   </div>
</template>

<script>
export default {
   data() {
       return {
       }
   },
   methods: {
       tel(){
           let device = weex.requireModule('device');
           // 设备id
           // let deviceId = device.deviceId();
           // 设备mac网络地址
           // let mac=device.mac();
           device.tel('15823961672')
       }
   }
}
</script>

<style scoped>
.content{
    padding-left: 30px;
    padding-right: 30px;
    padding-top: 60px;
}
.content_text{
    color: #666666;
    font-size: 28px;
}
</style>
