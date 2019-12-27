<template>
  <div>
    <headerNav title="使用手册" rightButton></headerNav>
    <scroller>
      <div class="wraper">
        <text class="txt" style="margin-top:38px;">代办点主要功能是：入库-通知-出库</text>
        <text class="txt">快递员主要功能是：入库-通知</text>
        <text class="txt">其他辅助功能：滞留件、短信历史、退件历史、查询、快件历史、客服</text>
        <text class="txt" style="margin-bottom:38px;">下面来介绍一下快递员和驿站的操作流程：</text>
        <div>
          <text class="title" @click="cut(0)">一、快递员扫描</text>
          <div v-if="isArr[0]">
            <text class="option">1、进入扫描功能，选择短信模板，不选择的话是系统默认短信，输入放置地址。</text>
            <image class="image" src="root:img/sm.png" alt=""></image>
            <text class="option">2、扫描时，条形码损坏，直接可以手动编辑。订单号和快递公司，手机号和客户姓名都是自动识别也可以手动输入。</text>
            <text class="option">3、确认入库，结束记录。</text>
            <image class="image" src="root:img/rk.png" alt=""></image>
            <text class="option">4、快递员只需扫描一次。</text>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(1)">二、驿站入库</text>
          <div v-if="isArr[1]">
            <text class="option">1、驿站开始扫描，扫描包裹时，根据包裹所在集件包，每扫一个包裹数量将会减少。如果当前集件包的包裹没有扫描完，扫描了其他包裹后，如果又扫描到了当前集件包，系统将会自动减少。在所有扫描完成后，结束入库，跳转到入库快件记录页面，有异常和集件包没有扫到的缺少件将展示出来。点击入库数量，发送短信，所有快件入库成功。</text>
            <image class="image" src="root:img/rk2.png" alt=""></image>
            <text class="option">2、若驿站扫描的快件是没有集件的包裹。操作和集件包裹时一样的，只是没有集件包裹数。</text>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(2)">三、驿站出库</text>
          <div v-if="isArr[2]">
            <text class="option">1、驿站进行扫描出库，扫描后将会弹出快件的详情信息，确认出库。</text>
            <image class="image" src="root:img/chuku.png" alt=""></image>
            <text class="option">2、如果条形码破损时，可以手动输入运单号、客户姓名客户电话、货号查找出库。</text>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(3)">四、滞留件</text>
          <div v-if="isArr[3]">
            <image class="image" src="root:img/zlj.png" alt=""></image>
            <text class="option">1、滞留件：快件在驿站48小时内没有被领取。系统将会自动提示。如果快递员和驿站都下了收发裹驿站，双方都会提示。</text>
            <text class="option">2、有滞留件时，驿站可以再通知客户来领取快件。如果滞留件超过168小时，系统将会提示驿站是否退件处理，如要退件处理先要通知快递员，并退件处理。</text>
            <image class="image" src="root:img/zlj3.png" alt=""></image>
            <image class="image" src="root:img/zlj2.png" alt=""></image>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(4)">五、退件处理</text>
          <div v-if="isArr[4]">
            <image class="image" src="root:img/tjcl.png" alt=""></image>
            <text class="option">1、滞留的快件添加到退件处理中的需要退件快递中，快递详情中确认退件。</text>
            <text class="option">2、可以直接扫描退件处理。</text>
            <image class="image" src="root:img/xytjcl2.png" alt=""></image>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(5)">六、短信模板</text>
          <div v-if="isArr[5]">
            <text class="option">1、系统将会默认一个模板。</text>
            <text class="option">2、可以自己增加短信模板。</text>
            <image class="image" src="root:img/dxmb.png" alt=""></image>
            <text class="option">3、快递员短信模板</text>
            <image class="image" src="root:img/dxmb2.png" alt=""></image>
            <text class="option">4、驿站短信模板</text>
            <image class="image" src="root:img/dxmb3.png" alt=""></image>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(6)">八、快件历史</text>
          <div v-if="isArr[6]">
            <text class="option">1、快件历史是记录快件状态的，根据搜索日期、快递状态和快递公司来查询快件的记录。</text>
            <image class="image" src="root:img/kjls.png" alt=""></image>
            <text class="option">2、快递公司只展示入过库的快递公司。</text>
            <image class="image" src="root:img/kjlskdgs.png" alt=""></image>
            <text class="option">3、驿站未下载时，默认展示全部状态，点击其他状态为空。</text>
            <image class="image" src="root:img/kjls2.png" alt=""></image>
          </div>
        </div>
        <div>
          <text class="title" @click="cut(7)">九、意见反馈</text>
          <div v-if="isArr[7]">
            <text class="option">如果有任何意见，都可以向我们提出。</text>
            <image class="image" src="root:img/tjfk.png" alt=""></image>
          </div>
        </div>
      </div>
    </scroller>
  </div>
</template>

<script>

export default {
  data() {
    return {
      isArr: [false, false, false, false, false, false, false,false],  // 一级列表的切换
    };
  },
  methods: {
    cut(i){
      let newArr = JSON.parse(JSON.stringify(this.isArr))
      newArr[i] = !this.isArr[i];
      this.isArr = newArr
    }
  }
};
</script>

<style scoped>
.wraper{
  padding-left: 30px;
  padding-right: 30px;
}
.txt{
  margin-top:58px;
  color: #333;
  font-size: 30px;
}
.title{
  height: 78px;
  font-size: 40px;
  line-height: 78px;
}
.title:active{
  background-color: #e5e5e5;
}
.option{
  color: #333;
  margin-bottom: 40px;
  font-size: 30px;
}
.image{
  height: 1228px;
  margin-bottom: 30px;
  border-width: 2px;
  border-style: solid;
  border-color: #ccc;
}
</style>
