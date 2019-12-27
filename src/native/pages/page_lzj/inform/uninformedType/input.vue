<template>
  <div class="wrap">
    <div class="main">
      <input ref="input"
             class="select"
             :type="type || 'text'"
             v-model="input"
             :placeholder="placeholder || '输入快递公司/电话号码/货号'"
             @return="select"/>
      <div class="selectButton" :style="baseStyle.fCenter" @click="select">
        <text style="color: #fff;font-size: 44px;">查询</text>
      </div>
    </div>
  </div>
</template>

<script>
import baseStyle from "../../../../common/baseStyle.js";
export default {
  data() {
    return {
      baseStyle,
      input: ''
    };
  },
  props: ['placeholder', 'type', 'today'],
  methods: {
    select(){
      let value = this.input;
      if (value.trim().length === 0){
          this.toast('查询内容不能为空');
          return
      }

      // 今日入库 今日出库 库存量中查询需判断查询条件
      // if (this.today) value = this.identifyValue(value);

      this.$emit('select', value);
      let timer = setTimeout(()=>{
          this.$refs['input'].blur();
          clearInterval(timer)
      }, 200)
    },
    /**
     * 鉴别值属于哪种查询
     * @param value
     * @returns {String}
     */
    identifyValue(value){
      if (/^\d{11}$/.test(value)){
        value = value + '&type&1' // phone
      } else if(/^\d{8}$/.test(value)) {
        value = value + '&type&2' // number
      } else {
        value = value + '&type&3' // com
      }
      return value;
    }
  },
  watch: {
    input(value){
      this.$emit('getInput', value)
    }
  }
};
</script>


<style scoped>
.wrap{
  align-items: center;
}
.main{
    flex-direction: row;
    width: 688px;
}
.select{
    width: 508px;
    height: 88px;
    padding-left: 20px;
    background-color: #fff;
    border-width: 2px;
    border-style: solid;
    border-color: rgba(0,119,226,1);
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
}
.selectButton{
    width:180px;
    height:88px;
    background-color:rgba(0,119,226,1);
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
}
</style>
