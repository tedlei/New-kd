<template>
   <div class="head_list">
      <div class="cell" v-for="(item, i) in dataList" :key="i" @click="toPage(i)">
        <text class="singular fs28">{{item.nav_text}}</text>
        <text class="even_numbers fs28">{{item.num}}</text>
      </div>
    </div>
</template>

<script>
export default {
   data() {
       return {
       }
   },
   props: ['dataList'],
    methods: {
      toPage(i){
        if (i === 2) {
          this.push('pages/page_lm/historyQuery/historyQuery', {from: '库存量'})
        } else if (i === 0){
            // 今日入库
            this.push('pages/page_lzj/index/todayInventory/todayInventory')
        } else if ( i === 1 ) {
            // 今日出库
            this.push('pages/page_lzj/index/todayInventory/todayOutbound')
        }
      }
    }
}
</script>

<style scoped src='./header.css' ></style>
