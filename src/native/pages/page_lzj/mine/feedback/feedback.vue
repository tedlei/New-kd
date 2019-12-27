<template>
  <div style="align-items: center;">
    <headerNav title="意见反馈" rightButton></headerNav>
    <textarea
            ref="textarea"
            class="textarea"
            cols="30"
            rows="10"
            placeholder="请输入您的意见"
            v-model="value"></textarea>
    <wxc-button 
      text="确定"
      type="blue"
      style="margin-top: 40px;margin-bottom: 38px;"
      :isHighlight="true"
      @wxcButtonClicked="wxcButtonClicked"></wxc-button>
    <div style="width: 648px;">
      <text class="tit">意见反馈历史</text>
      <scroller :style="{height: dh - 780+ 'px'}">
        <div v-for="(value, i) in msgList" :key="i">
          <text style="margin-top: 28px;margin-bottom: 20px;color: #999;">{{value.userTime}}</text>
          <text style="color: #999;">{{value.content}}</text>
        </div>
      </scroller>
    </div>
  </div>
</template>

<script>
import { WxcButton } from "weex-ui";
import infoVerify from '../../../../common/infoVerify';
export default {
  components: { WxcButton },
  data() {
    return {
      value: "",
      msgList: [],
      dh: ""
    };
  },
  methods: {
    wxcButtonClicked() {
      let value = this.value;
      if (!value) {
        this.toast("提交内容不能为空");
        return;
      }
      this.modal.confirm(
        {
          message: "是否确认提交",
          okTitle: "确认",
          cancelTitle: "取消"
        },
        result => {
          if (result === "取消") return;
          // 请求添加接口
          this.fetch(
            {
              url: "orderIdea/feedback",
              body: {
                content: value
              }
            },
            result => {
              let msg = "提交成功";
              if (result.res.code === 200) {
                this.msgList = result.res.data1.data;
              } else {
                msg = "提交失败";
              }
              this.toast(msg);
            }
          );
          // 请求成功
          // 清空内容 并失去焦点
          this.value = "";
          this.$refs.textarea.blur();
          // 更新msgList
          this.msgList.push({
            timer: JSON.stringify(new Date()).substring(1, 11),
            context: "意见内容：还不错的软件"
          });
        }
      );
    }
  },
  created() {
    this.dh = this.getScreenHeight();

    this.fetch(
      {
        url: "orderIdea/idea",
        body: {}
      },
      result => {
        // 请求成功 更新msgList
        if (result.res.code === 200) {
          this.msgList = result.res.data;
        } else {
          this.toast("网络错误");
        }
      }
    );
  },
  watch:{
    //过滤表情包
    'value':function(value){
      let sett = setTimeout(()=>{
        this.value = infoVerify.onInput(value);
        clearTimeout(sett);
      },0)
    }
  }
};
</script>

<style scoped>
.textarea {
  width: 690px;
  height: 400px;
  margin-top: 20px;
  padding: 20px;
  border-width: 2px;
  border-style: solid;
  border-color: #ccc;
}
.tit {
  font-size: 34px;
  color: rgba(51, 51, 51, 1);
}
</style>
