<template>
  <div style="background-color:#f2f2f2;">
    <headerNav title="设置" rightButton></headerNav>
    <div
      v-for="(item, i) in contentList"
      :key="i"
      style="background-color:#fff;"
      :style="{marginBottom: i === 3 ? 0 : '30px'}"
    >
      <div v-if="item.name" class="content borBm" @click="toPage(item.url, i)">
        <text class="content_text">{{item.name}}</text>
        <text v-if="i !== 3" style="font-size: 28px;">＞</text>
      </div>
      <div v-if="item.name2" class="content" @click="toPage(item.url2)">
        <text class="content_text">{{item.name2}}</text>
        <text style="font-size: 28px;">＞</text>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      contentList: [
        {
          name: "入库设置", // 快递员时 赋值为false
          name2: "出库设置",
          url: "pages/page_lzj/mine/setting/rukuSetting",
          url2: "pages/page_lzj/mine/setting/chukuSetting"
        },
        {
          name: "滞留件设置",
          name2: "退件设置",
          url: "pages/page_lzj/mine/setting/zljSetting",
          url2: "pages/page_lzj/mine/setting/tuijianSetting"
        },
        // {
        //   name: "失败短信提醒",
        //   name2: "模板审核提醒",
        //   url: "pages/page_lzj/mine/setting/shibaiDXSetting",
        //   url2: "pages/page_lzj/mine/setting/mobanSHSetting"
        // },
        {
          name: "退出登陆",
          name2: false,
          url: "pages/page_lzj/mine/setting",
          url2: "pages/page_lzj/mine/setting"
        }
      ]
    };
  },
  methods: {
    toPage(url, i){
      if (i === 2) {
        return this.currentQuit()
      }
      this.push(url)
    },
    currentQuit(){
      this.modal.confirm({
        message: '退出账号 ?',
        okTitle: '确定退出',
        cancelTitle: '取消',
        duration: 1
      }, (val)=>{
        if (val === '确定退出') {
          // 去除session 删除本地 storage
          this.quit()
        }
      })
    }
  },
  created(){
    this.getItem('userInfo', result => {
      if (result.data.userRole === '1'){
        this.contentList[0].name2 = false;
        // this.contentList[0].name2 = '扫描设置';
      }
    })
  }
};
</script>

<style scoped>
.content {
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  height: 100px;
  padding-right: 30px;
}
.content:active{
  background-color: #e5e5e5;
}
.borBm {
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
}
.content_text {
  padding-left: 30px;
  color: #333;
  font-size: 28px;
}
</style>
