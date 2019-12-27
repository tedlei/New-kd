<template>
  <div class="head" :style="{height}">
    <div class="searchbar" @click="toSelect">
      <text class="iconfont bar-ic">&#xe634;</text>
      <text class="searchbarContent">请输入运单号、手机号、姓名、货号查询</text>
    </div>
    <headList v-if="id" :dataList="dataList"></headList>
    <div v-else  :style="{flexDirection: 'row', justifyContent: 'space-between', width: '690px'}">
      <div  class="tit" :style="{marginRight: '40px'}" @click="toPage(1)">
        <text class="titText">今日扫描：</text>
        <text class="titText">{{dataList.daycount}}</text>
      </div>
      <div class="tit" @click="toPage(2)">
        <text class="titText">库存量：</text>
        <text class="titText">{{dataList.repertory}}</text>
      </div>
    </div>
  </div>
</template>

<script>
import headList from './head_list.vue'
export default {
  components: {headList},
  data() {
    return {
      id: true,
      height: '',
      dataList: {}
    };
  },
  async created() {
      // true 为代办点   false为快递员
      this.id = await this.verdictId();

      this.height = this.id ? '224px' : '160px';

      // 初始执行一次
      this.dataList = await this.init();

      // 页面显示更新
      await this.onResume(this.init, 'dataList');
  },
  methods: {
    toSelect(){
      // this.getItem('userInfo', result => {
      //   let param = [result.data.sessionId];
      //   param.push(result.data.userRole*1);
      //   this.pushAndroid('openSearchPackageActivity', param);
      // });
      this.push("pages/page_lm/inquire/inquireQueryChild")
    },
    init(){
      return new Promise(resolve => {
        this.fetch({
          url: 'manager/order/selectcount',
          body: {}
        }, (result) => {
          let obj = {},
                  res = result.res;
          if (this.id) {
            obj = [
              {
                nav_text: '今日入库',
                num: 0
              },
              {
                nav_text: '今日出库',
                num: 0
              },
              {
                nav_text: '库存量',
                num: 0
              }
            ]
            obj[0].num = res.daycount;
            obj[1].num = res.outbound;
            obj[2].num = res.repertory
          } else {
            obj.daycount = res.daycount;
            obj.repertory = res.repertory
          }
          resolve(obj)
        })
      });
    },
    toPage(val){
      if (val === 1) {
        this.push('pages/page_lzj/index/todayInventory/todayInventory')
      } else this.push('pages/page_lm/historyQuery/historyQuery', {from: '库存量'})

    }
  }
};
</script>
<style scoped src='./header.css'></style>
<style scoped>
  .tit{
    flex-direction: row;
    align-items: center;
    height: 80px;
    background-color: #0077e2;
  }
  .titText{
    color: #fff;
    font-size: 28px;
  }
</style>
