<template>
 <div class="st-app">
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
  </div>
</template>

<script>
const modal = weex.requireModule("modal");
const dom = weex.requireModule("dom");
import bd from "../../../common/baseStyle.js";
export default {
  props: ["time", "isSelTime"],
  data() {
    return {
      bd,
      startTime: "", //开始时间
      endTime: "" //开始时间
    };
  },
  created() {
    this.startTime = this.getDate();
    this.endTime = this.getDate();
  },
  methods: {
    //获取时间
    getDate: function(boo) {
      let date = new Date();
      let y = date.getFullYear();
      let m = date.getMonth() + 1;
      let d = date.getDate();
      if (boo) {
        if (m - 6 <= 0) {
          y--;
          m = 12 - (6 - m);
        } else m = m - 6;
        return (
          "" + y + "-" + (m >= 10 ? m : "0" + m) + "-" + (d >= 10 ? d : "0" + d)
        );
      } else
        return (
          "" + y + "-" + (m >= 10 ? m : "0" + m) + "-" + (d >= 10 ? d : "0" + d)
        );
    },

    //时间比较
    compare: function(st, et) {
      st = st.replace(/-/g, "") * 1;
      et = et.replace(/-/g, "") * 1;
      if (st > et) {
        this.toast("开始时间不能大于结束时间！！");
        let sett = setTimeout(() => {
          this.startTime = this.endTime;
          clearTimeout(sett);
        }, 10);
      }
      this.$emit('selymd',st+'',et+'')
    }
  },
  computed: {
    maxTime: function() {
      return this.getDate();
    },
    minTime: function() {
      return this.getDate(true);
    }
  },
  watch: {
    startTime: function(val) {
      this.compare(val, this.endTime);
    },
    endTime: function(val) {
      this.compare(this.startTime, val);
    }
  }
};
</script>

<style scoped>
.st-app {
  margin-top:40px;
  width: 750px;
}
.stDivStartTime {
  width: 750px;
  height: 110px;
  padding-left: 30px;
  padding-right: 30px;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}
.stFont {
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.divStime {
  width: 520px;
  height: 72px;
  border-color: rgba(204, 204, 204, 1);
  border-radius: 10px;
  padding-right: 20px;
  flex-direction: row;
  justify-content: space-between;
}
.stTime {
  width: 516px;
  height: 68px;
  padding-left: 20px;
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
.stIcon {
  margin-top: 23px;
  margin-left: -40px;
}
</style>
