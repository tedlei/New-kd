<template>
  <div>
    <headerNav title="关于" rightButton></headerNav>
    <div class="hdb">
      <image
        style="width: 68px;height: 68px;margin-right: 30px;border-radius: 34px;"
        src="root:img/hdb.png"
        alt="">
      </image>
      <div>
        <text class="hdb_text">收发裹驿站</text>
        <text class="hdb_text">最新版本{{newVersion}}，无需更新</text>
      </div>
    </div>
    <div class="hdb_list">
      <text class="l_text">版本信息</text>
      <text class="l_text2">当前版本{{version}}</text>
    </div>
    <div class="hdb_list" @click="toPages('pages/page_lzj/mine/about/about_sub/termsOfService')">
      <text class="l_text">服务条款</text>
      <text class="l_text2">＞</text>
    </div>
    <div class="hdb_list" @click="toPages('pages/page_lzj/mine/about/about_sub/contactUs')">
      <text class="l_text">联系我们</text>
      <text class="l_text2">＞</text>
    </div>
    <div class="hdb_list last" @click="toPages('pages/page_lzj/mine/about/about_sub/QrCodeSharing')">
      <text class="l_text">二维码分享</text>
      <text class="l_text2">＞</text>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      version: "",
      newVersion: ''
    };
  },
  methods: {
    toPages(url){this.push(url)}
  },
  created() {
    let env = weex.requireModule('env');
    this.version = env.versionName();

    this.fetch({
      url: 'orderVersions/getVersions',
      body: {}
    }, result=>{
      this.newVersion = result.res.data.versionsnew;
    });
  }
};
</script>

<style scoped>
.hdb,
.hdb_list{
  flex-direction: row;
  align-items: center;
  height: 100px;
  padding-left: 30px;
  padding-right: 30px;
}
.hdb {
  margin-bottom: 20px;
  background-color: #e5f1f3;
}
.hdb_text{
  color: #0077E2;
  font-size:24px;
}
.hdb_list{
  justify-content: space-between;
  border-top-width: 2px;
  border-top-style: solid;
  border-top-color: #CCCCCC;
}
.l_text,
.l_text2{
  font-size: 28px;
}
.l_text{
  color: #333333;
}
.l_text2{
  color: #999;
}
.last{
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: #CCCCCC;
}
</style>
