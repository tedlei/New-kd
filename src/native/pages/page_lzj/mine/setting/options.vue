<template>
  <div>
    <div class="option" v-for="(item, i) in listData" :key="i" @click="cutOption(i)">
      <text style="font-size: 28px;">{{item.text}}</text>
      <div class="option_right" :class="[controlNum===i ? 'checked' : '']">
        <text
          style="position: absolute;top:4px;left:4px;font-size: 24px;font-family: iconfont;"
          :style="{color: controlNum===i ? '#fff' : 'transparent'}"
        >&#xe610;</text>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["listData", "controlNum", "HintClass"],
  methods: {
    cutOption(i) {
      this.controlNum = i;
      this.getItem("audio", result => {
        let obj = this.stringify(result.data);
        let boole = true;
        obj["noHint"][this.HintClass] = true;
        switch (i) {
          case 1:
            boole = false;
            obj["noHint"][this.HintClass] = true;
            break;
          case 2:
            boole = false;
            obj["noHint"][this.HintClass] = false;
        }
        switch (this.HintClass) {
          case "goOutHint":
            this.setItem("goOutHintNum", i);
            obj["pvtpInfo"].isHint = boole;
            obj["goOutSucceed"].isHint = boole;
            obj["goOutFailed"].isHint = boole;
            obj["alreadyGoOut"].isHint = boole;
            break;
          case "placeHint":
            this.setItem("placeHintNum", i);
            obj.goOutHintinfo = boole;
            this.pushAndroid('setIsPlay', [i])
            // obj["enterIntoSucceed"].isHint = boole;
            // obj["enterIntoFailed"].isHint = boole;
            // obj["placeSucceed"].isHint = boole;
            // obj["placeFailed"].isHint = boole;
            // obj["repetition"].isHint = boole;
            break;
          case "retentionHint":
            this.setItem("retentionNum", i);
            obj["retention"].isHint = boole;
            break;
          case "handBackHint":
            this.setItem("handBackNum", i);
            obj["handBackSucceed"].isHint = boole;
            obj["handBackFailed"].isHint = boole;
            break;
        }
        this.setItem("audio", obj);
      });
    }
  }
};
</script>

<style scoped>
.option {
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  height: 100px;
  padding-left: 30px;
  padding-right: 30px;
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
}
.option:active {
  background-color: #e5e5e5;
}
.option_right {
  width: 34px;
  height: 34px;
  border-width: 2px;
  border-style: solid;
  border-color: #999;
  background-color: transparent;
  line-height: 34px;
  border-radius: 17px;
}
.checked {
  border-width: 0;
  background-color: #0077e2;
}
</style>
