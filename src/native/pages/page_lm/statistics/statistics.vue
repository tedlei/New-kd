<template>
  <div class="st-app">
    <headerNav title = '快件统计' rightButton="" ></headerNav>
    <div class="stDivStartTime">
      <text class="stFont">开始时间</text>
      <div class="divStime" :style="bd.borderWS">
        <input type="date" v-model="startTime" :max="maxTime" :min="minTime" class="stTime"></input>
        <text style="font-family:iconfont;" class="stIcon">&#xe622;</text>
      </div>
    </div>
    <div class="stDivStartTime">
      <text class="stFont">结束时间</text>
      <div class="divStime" :style="bd.borderWS" >
        <input type="date" v-model="endTime" :max="maxTime" :min="minTime" class="stTime"></input>
        <text style="font-family:iconfont;" class="stIcon">&#xe622;</text>
      </div>
    </div>
    
    <text class="stBtn" @click="topQuery">查询</text>
    <text class="stqtFont">统计结果</text>

    <ul class="stUl">
      <li class="stLi">
        <text class="stLiFont w31">快递公司</text>
        <text class="stLiFont w23">入库</text>
        <text class="stLiFont w23">出库</text>
        <text class="stLiFont w23">剩余</text>
      </li>
      
      <li class="stLiFlex">
        <scroller class="stScroller" v-if="queryList.length>0">
          <div class="stLi bgw" v-for="(e,i) of queryList" :key='i'>
            <text class="stLiFont w31">{{e.ordercompany}}</text>
            <text class="stLiFont w23">{{e.addorder}}</text>
            <text class="stLiFont w23">{{e.outorder}}</text>
            <text class="stLiFont w23">{{e.repertoryorder}}</text>
          </div>
        </scroller>
      </li>
      
      <li class="stLi borderTop">
        <text class="stLiFont w31">合计</text>
        <text class="stLiFont w23">{{amount}}</text>
        <text class="stLiFont w23">{{allout}}</text>
        <text class="stLiFont w23">{{allrepertory}}</text>
      </li>
    </ul>
  </div>
</template>

<script>
import bd from '../../../common/baseStyle.js'
export default {
  data() {
    return {
      bd,
      startTime:'',   //开始时间
      endTime:'',   //开始时间
      queryList:[],   //统计列表
      amount:0,   //总库存
      allrepertory:0,   //总未出库
      allout:0,   //总已出库
    };
  },

  mounted(){
    this.startTime = this.getDate();
    this.endTime = this.getDate();
  },

  methods:{
    //获取时间
    getDate:function(boo){
      let date = new Date();
      let y = date.getFullYear();
      let m = date.getMonth()+1;
      let d = date.getDate();
      if(boo){
        if(m-6<=0){
          y--;
          m = 12 - (6-m);
        }else m = m-6;
        return ''+y+'-'+(m>=10?m:'0'+m)+'-'+(d>=10?d:'0'+d);
      }else return ''+y+'-'+(m>=10?m:'0'+m)+'-'+(d>=10?d:'0'+d);
    },


    //时间比较
    compare:function(st,et){
      st = st.replace(/-/g,'')*1;
      et = et.replace(/-/g,'')*1;
      if(st>et){
        this.toast('开始时间不能大于结束时间！！');
        let sett = setTimeout(()=>{
          this.startTime = this.endTime;
          clearTimeout(sett);
        },10)
      }
    },

    //点击查询
    topQuery:function(){
      let url = 'manager/order/selectorderstatus';
      let body = {starttime:this.startTime.replace(/-/g,''), endtime:this.endTime.replace(/-/g,''),statistical:"1",index:'-1'} ;
      this.queryList = [];
      this.amount = 0;
      this.allrepertory = 0;
      this.allout = 0;
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code=== 200) {
            if(JSON.stringify(obj.data)==='{}'){
              this.toast("没有数据！！")
              return
            }
            this.amount = obj.amount
            this.allrepertory = obj.allrepertory
            this.allout = obj.allout
            this.objConvert(obj.data)
          } else if (obj.code === 401) {
            this.toast(obj.message);
          }
        },
        err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        }
      );
    },

    objConvert:function(obj){
      let arr = [];
      for(let i in obj){
        arr.push(obj[i]);
      }
      this.queryList = arr;
    }

  },
  computed:{
    'maxTime':function(){
      return this.getDate();
    },
    'minTime':function(){
      return this.getDate(true);
    },
  },
  watch: {
    'startTime':function(val){
      this.compare(val,this.endTime);
    },
    'endTime':function(val){
      this.compare(this.startTime,val);
    }
  },
};
</script>
<style scoped src='./statistics.css'></style>