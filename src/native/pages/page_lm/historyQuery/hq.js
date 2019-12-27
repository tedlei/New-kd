import bs from "../../../common/baseStyle.js";
import courierList from '../courierList/courierList.vue'
import selListCom from "./selList.vue";
import selTime from "./selTime.vue";
export default {
  components: { selListCom, selTime, courierList },
  data() {
    return {
      bs, // 公用样式
      timeNum: 0, //时间状态
      isShowTime: false, //是否显示自定义时间
      // isSelTime: 0+"无用", //是否打开开始时间选择
      isShowimg: 0,   //是否显示加载图标
      ischicked1: 0, //时间选择
      ischicked2: 0, //状态选择
      ischicked3: 0, //公司选择
      selstart: 0,  //获取数据时保留条件状态
      userRole: 1, //是否为快递员
      hqSelNum: 1,  //状态控制
      hqList: [],   //查询后的列表
      sstime: '',   //显示开始时间
      setime: '',   //显示结束时间
      selrepertory: "", //选择库存
      selcompany: "", //选择公司
      startTime: "", //开始时间
      endTime: "", //结束时间
      repertory:'',  //
      selList: [
        [
          { title: "今天", num: 1 },
          { title: "最近一周", num: 7 },
          { title: "最近一个月", num: 30 },
          { title: "自定义时间", num: 0 }
        ],
        [
          { title: "", num: "" },
          { title: "未出库", num: "1" },
          { title: "取件出库", num: "2" },
          { title: "退件出库", num: "4" }
        ],
        [
          { title: "" }
        ]
      ],
    };
  },

  mounted() { 
    this.getItem("userInfo", res => {
      if (res.result==='success') this.userRole = res.data.userRole*1;
    });
    this.getTime(1, 0);
    this.getLocahostRoCompany();
  },
  methods: {
    //横向选择条件时
    topDiv: function (num) {
      this.hqList = [];
      if (num === 1 && this.ischicked1 === 3) {
        this.isShowTime = true;
      } else {
        this.isShowTime = false;
      }
      this.hqSelNum = num;
    },

    //纵向选择条件时
    selcheck: function (listNum, hqSelNum) {
      let tit = this.selList[listNum][hqSelNum];
      switch (listNum) {
        case 0:
          this.ischicked1 = hqSelNum;
          this.getTime(tit.num, listNum);
          break;
        case 1:
          this.ischicked2 = hqSelNum;
          this.selrepertory = tit.num;
          break;
        case 2:
          this.ischicked3 = hqSelNum;
          if(this.userRole===1)this.selcompany = tit.title
          else this.selcompany = tit.title;
          break;
      }
    },
    //获取结束时间
    getTime(number, listNum) {
      var date = new Date(); //获取当前时间
      var y = date.getFullYear(); //获取年
      var m = date.getMonth() + 1; //获取月
      var d = date.getDate(); //获取日
      var num = date.getTime();
      if (number !== 0 && number !== 1) {
        this.endTime = "" + y + (m < 10 ? '0' + m : m) + (d < 10 ? '0' + d : d);
        var date = new Date(num - number * 24 * 60 * 60 * 1000);
        this.startTime =
          "" + date.getFullYear() +
          (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) +
          (date.getDate() < 10 ? "0" + date.getDate() : date.getDate());
      } else {
        this.startTime = "" + y + (m < 10 ? '0' + m : m) + (d < 10 ? '0' + d : d);
        this.endTime = "" + y + (m < 10 ? '0' + m : m) + (d < 10 ? '0' + d : d);
      }
      if (number !== 0) {
        this.sstime = '';
        this.setime = '';
      }
      if (listNum === 0) {
        if (number === 0) {
          this.isShowTime = true;
        } else {
          this.isShowTime = false;
        }
      }
    },

    //自定义选择年时
    selymd: function (startTime, endTime) {
      this.startTime = startTime
      this.endTime = endTime
    },

    //点击查询时
    queryCourier: function (num) {
      if (num === '0') {
        this.hqList = [];
        num = '1';
      }
      if(this.repertory==="库存量"){
        let item = new Date(); 
        var date = new Date(item.getTime() - (180 * 24 * 60 * 60 * 1000));
        this.startTime =
          "" + date.getFullYear() +
          (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) +
          (date.getDate() < 10 ? "0" + date.getDate() : date.getDate());
      }
      let url = 'manager/order/selectorderstatus';
      let body = { starttime: this.startTime, endtime: this.endTime, company: this.selcompany, orderstatus: this.selrepertory, index: num };
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (obj.data.length > 0) {
              this.hqList = this.hqList.concat(obj.data);
              this.hqSelNum = 0;
              this.isShowTime = false;
              this.isShowimg = 0;
            } else {
              this.isShowimg = 2;
              this.toast('没有更多数据！！')
            }
          } else if (obj.code === 401) {
            this.toast(obj.message);
          }
        },
        err => {
          if (!err) {
            this.toast("网络错误,请检查您的网络！！！");
          }
        },true
      );
    },


    //获取代办点地址或快递公司
    getLocahostRoCompany: function () {
      let url = 'manager/order/getcompany';
      let body = {}
      this.fetch(
        { url, body },
        res => {
          let obj = res.res;
          if (obj.code === 200) {
            if (obj.data.length > 0) {
              let arr = obj.data;
              for (let i of arr) {
                let o = {};
                o.title = i;
                this.selList[2].push(o);
              }
            }
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

    //加载更多
    onLoadmore: function () {
      this.isShowimg = 1;
      let num = Math.ceil(this.hqList.length / 10) + 1
      this.queryCourier(num.toString());
    }
  },

  async created() {
    let a = await this.getParam()
    let str = a.from;
    this.repertory = str;
    if(str==="库存量"){
      this.hqSelNum = 2;
      this.ischicked2 = 1;
      this.selrepertory = '1';
      this.queryCourier("0");
    }
  },
};