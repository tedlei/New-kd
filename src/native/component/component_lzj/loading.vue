<template>
  <div v-if="open" class="loading" :style="{
      transform: 'rotate(' + value + 'deg)',
      top: dh/2 - 30 + 'px'
    }"></div>
</template>

<script>
    export default {
        data() {
            return {
                dh: 0, // 设备高度
                value: 0, // 旋转度数
                open: false  // 是否加载中
            }
        },
        methods: {
            openLoading(){
                this.open = true;
                // 初始执行一次动画
                this.value = 360;

                // 没过一秒再次执行
                let timer = setInterval(()=>{
                    if (!this.open) {
                        clearInterval(timer);
                        return;
                    }
                    this.value+=360
                }, 500)
            },
            closeLoading(){
                this.open = false;
                this.value = 0;
            }
        },
        created(){
          this.dh = this.getScreenHeight()
        }
    }
</script>

<style scoped>
.loading{
  position: fixed;
  left: 345px;
  width: 60px;
  height: 60px;
  border-width: 10px;
  border-style: solid;
  border-color: #F2F2F2;
  border-left-color: #0077E2;
  border-radius: 30px;
  transition: transform;
  transition-duration: 500ms;
  transition-timing-function: linear;
}
</style>