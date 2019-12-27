<template>
  <div>
    <headerNav :title="id ? '驿站信息' : '个人信息'" @headerEvent="headerEvent" rightButton="完成"></headerNav>
    <div class="list borBottom">
      <text class="list_text_left fs28" style="color:#999;">用户名</text>
      <text class="fs28" style="height: 100px;color:#999;line-height: 100px;">{{userPhone}}</text>
    </div>
    <div class="list borBottom" :style="{borderBottomWidth: '30px',borderBottomColor: '#f2f2f2'}">
      <text class="list_text_left fs28">{{id ? '驿站名称' : '快递员'}}</text>
      <label>
        <input ref="input1" class="input fs28" type="text" v-model="nickname" :value="nickname">
      </label>
    </div>
    <div class="list borBottom">
      <text class="list_text_left fs28">{{id ? '详细地址' : '配送范围'}}</text>
      <label>
        <input ref="input2" class="input fs28" type="text" v-model="address" :value="address">
      </label>
    </div>
  </div>
</template>
<style scoped>
  .borBottom{
    border-bottom-width: 2px;
    border-bottom-style: solid;
    border-bottom-color: #CCC;
  }
  .fs28{
    font-size: 28px;
  }
  .list{
    flex-direction: row;
    align-items: center;
    padding-left: 30px;
    padding-right: 30px;
  }
  .list_text_left{
    width: 184px;
  }
  .input{
    width: 504px;
    height: 100px;
  }
</style>
<script>

export default {
  data() {
    return {
      id: true, // 身份判断
      oldNickname: '',  // 保存修改之前的用户名
      aldAddress: '',   // 保存修改之前的地址
      userPhone: '',  // 用户电话
      nickname: '',   // 用户名
      address: '',    // 地址
    };
  },
  methods: {
    headerEvent(value){
      if (value === 'left') return;
      let {nickname, address, oldNickname, aldAddress} = this;
      if (nickname === oldNickname && address === aldAddress) {
        this.toast('未作任何修改');
        return
      }
      this.fetch({
        url: 'orderUser/updateuser',
        body: {
          nickname: nickname,
          address: address
        }
      }, (result)=>{
          let data = result.res.data;
          this.setItem('userInfo', result.res.data);  // 更新本地用户数据
          this.update();  // 更新
          this.toast('修改完成');
          this.$refs.input1.blur();  // 失去焦点
          this.$refs.input2.blur();  // 失去焦点
      })
    },
    async update(){
      this.id = await this.verdictId();

      this.getItem('userInfo', (result)=>{
        let userInfo = result.data;
        this.userPhone = userInfo.userPhone;
        this.nickname = userInfo.userName;
        this.address = userInfo.address;

        // 存储旧数据  用于判断当数据未更改时点击完成是否拦截
        this.oldNickname = userInfo.userName;
        this.oldAddress = userInfo.address;
      });
    }
  },
  created() {this.update()}
};
</script>
