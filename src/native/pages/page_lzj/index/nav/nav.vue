<template>
  <div class="nav">
    <div class="nav_tit">
      <div class="cell" @click="toPage(1)">
        <text class="iconfont icon nav_tit_sub" :style="{fontSize: '88px'}">&#xe676;</text>
        <div class="border_right"></div>
        <text class="even_numbers nav_tit_sub">入库</text>
      </div>
      <div class="cell" @click="toPage(2)">
        <text class="iconfont icon nav_tit_sub" :style="{fontSize: id ? '88px': '78px'}">{{id ? '&#xe639;' : '&#xe63d;'}}</text>
        <div class="border_right"></div>
        <text class="even_numbers nav_tit_sub">{{id ? '通知' : '派件历史'}}</text>
<!--        <text class="nav_tit_sub2" :style="{right: userRole ? '30px' : '92px'}">(短信)</text>-->
      </div>
      <div v-if="userRole" class="cell" @click="toPage(3)">
        <text class="iconfont icon nav_tit_sub" :style="{fontSize: '88px'}">&#xe675;</text>
        <text class="even_numbers nav_tit_sub">出库</text>
      </div>
      <!-- border_bottom -->
      <div class="border_bottom"></div>
    </div>
    <mainList :nav_main_list="nav_main_list" @getListNum="getListNum" option-size=""></mainList>
  </div>
</template>

<script>
import mainList from './main_list.vue'

export default {
  components: {mainList},
  data() {
    return {
      id: true, // true 代办点  false快递员
      userRole: true,
      nav_main_list: [],
      userInfo: []
    };
  },
  methods: {
    getListNum:function(){
      let url ="orderUser/getProduct";
      this.fetch({url,body:{}},res=>{
        let obj = res.res;
        if(obj.code===200){
          this.nav_main_list[2].num = obj.data?''+obj.data:'';
        }else{
         this.toast(obj.message)
        }
      },()=>{
        this.toast("网络错误，请检查您的网络！！")
      },true)
    },
    toPage(number) {
      if (number === 2) {
        let url = 'pages/page_lzj/inform/inform';
        if (!this.id) url = 'pages/page_lm/historyQuery/historyQuery';
        this.push(url, {from: '派件历史'});
        return ;
      }
      let url = '', // 页面地址
          params = [],  // 传递参数
          {id, userInfo} = this;
      switch (number) {
        case 1:
          if (id) {
            // 代办点
            url = 'openScanerActivity';
            // 传递参数 姓名 电话  token
            params.push(userInfo.address);
          } else {
            // 快递员
            url = 'openExScanerActivity';
            // 传递参数 姓名 电话  token
            params.push(userInfo.userName);
            params.push(userInfo.userPhone);
          }
          break;
        case 3:
          // 代办点出库
          url = 'openOutLibActivity';//token
              break;
      }
      params.push(userInfo.sessionId);
      this.pushAndroid(url, params);
    },
    // 初始化函数
    initData(userRole){
        let data = [
            {
                url: 'pages/page_lm/retention/retention',
                icon: "\ue635",
                text: "滞留件",
                color: "#e9784c",
                num: ''
            },
            {
                url: 'pages/page_lzj/index/messageHistory/messageHistory',
                icon: "\ue642",
                text: "短信历史",
                color: "#0077e2",
                num: ''
            },
            {
                url: 'pages/page_lm/quitPiece/quitPiece',
                icon: "\ue632",
                text: "退件处理",
                color: "#ff4666",
                num: ""
            },
            {
                url: 'pages/page_lm/inquire/inquireQueryChild',
                icon: "\ue62a",
                text: "查询",
                color: "#6cc27c",
                num: ''
            },
            {
                url: 'pages/page_lm/historyQuery/historyQuery',
                icon: "\ue63a",
                text: "收件历史",
                color: "#0077e2",
                num: ''
            },
            {
                url: 'pages/page_lzj/index/customerService/customerService',
                icon: "\ue630",
                text: "客服",
                color: "#6cc27c",
                num: ''
            },
            {
                url: 'pages/page_lzj/mine/topUpRecharge/topUpRecharge',
                icon: "\ue627",
                text: "充值",
                color: "#0077e2",
                num: ''
            }
        ];
        // 身份为快递员
        if (!userRole) {
            data[4].icon = '\ue637';
            data[4].text = '通知';
            data[4].url = 'pages/page_lzj/inform/inform';

            data=data.concat([{
                url: 'pages/page_lm/misplace/misplace',
                icon: "\ue631",
                text: "异常件",
                color: "#ff4666",
                num: ''
            },{
                url: 'pages/page_lm/site/site',
                icon: "\ue63c",
                text: "添加代办点",
                color: "#0077e2",
                num: ''
            }]);
            data[2].text = '快件通知';
        }else{
          data=data.concat([{
                url: 'pages/page_lm/statistics/statistics',
                icon: "\ue62d",
                text: "快件统计",
                color: "#6cc27c",
                num: ''
            }])
        }
        this.nav_main_list = data;
    }
  },
  async created() {
      this.getItem('userInfo', result => {
          let userRole = this.userRole;
          userRole = result.data.userRole === '2';
          this.userRole = userRole;
          // 初始化数据
          this.initData(userRole);
          if(!userRole){
            //快递员获取退件数量
            this.getListNum()
          }
      });

      // 获取身份id
      this.id = await this.verdictId();

      // 获取用户信息
      this.getItem('userInfo', result=>{
        this.userInfo = result.data;
      });
  }
};
</script>

<style scoped src='./nav.css'></style>
