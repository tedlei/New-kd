<template>
  <div :style="{flex: 1, height: dh + 'px'}">
    <headerNav title="设为不通知" @headerEvent="headerEvent" right-button :back="false"></headerNav>
    <div style="flex: 1;justify-content:space-between;background-color:#F2F2F2;" >
      <div style="flex: 1;">
        <scroller :style="{height: dh - 390 + 'px'}">
          <div class="list" v-for="(item, i) in listData" :key="i" @click="cutStatus(i)">
            <div class="list_top">
              <text style="color: #333333;fontSize: 30px;"
              >{{item.company}}&nbsp;&nbsp;{{item.orderId}}</text>
              <text style="color: #999999;fontSize: 30px;">货号&nbsp;&nbsp;{{item.shelves}}</text>
              <div
                class="list_option"
                :class="[isArr[i] ? 'checked' : '']"
                :style="baseStyle.fCenter"
              >
                <text
                  v-if="isArr[i]"
                  style="color:#fff;font-size: 26px;font-family: iconfont;"
                >&#xe614;</text>
              </div>
            </div>
            <div class="list_bottom">
              <text style="color: #999999;fontSize: 30px;">电话&nbsp;&nbsp;{{item.expressOwnerTel}}</text>
            </div>
          </div>
        </scroller>
      </div>
      <div class="list_last" :style="baseStyle.fCenter">
        <text :style="baseStyle.fs30" style="color: #999999;">共计: {{listData.length}}条</text>
      </div>
    </div>
    <div class="footer">
      <div class="footer_left" @click="checkAll">
        <div class="list_option" :class="[iscCheckAll ? 'checked' : '']" :style="baseStyle.fCenter">
          <text
            v-if="iscCheckAll"
            style="color:#fff;font-size: 26px;font-family: iconfont;"
          >&#xe614;</text>
        </div>
        <text :style="baseStyle.fs30">全选</text>
      </div>
      <div class="footer_right" :style="baseStyle.fCenter">
        <text style="color: #fff;font-size: 44px;" @click="makeSure">设为不通知</text>
      </div>
    </div>
  </div>
</template>

<script>
import baseStyle from "../../../../common/baseStyle.js";
import select from '../select';

export default {
  data() {
    return {
      dh: 0,
      baseStyle,
      isArr: [], // 用于判断的数据
      iscCheckAll: false // 控制全选
    };
  },
  props: ["listData"],
  methods: {
    // 单个切换
    cutStatus(i) {
      select.cutStatus.call(this, i)
    },
    // 全选按钮
    checkAll() {
      select.checkAll.call(this)
    },
    deleteListData() {
      let isArr = this.stringify(this.isArr),
        listData = this.stringify(this.listData),
        deleteData = [];
      for (let i = isArr.length - 1; i > -1; i--) {
        // 选择则删除对应项
        if (isArr[i]) {
          // 获取删需除数据
          deleteData.push(listData[i]);
          isArr.splice(i, 1);
          listData.splice(i, 1);
        }
      }
      // 调用接口删除列表数据
      this.fetch({
          url: '/manager/order/noinform',
          body: {
              orderId: deleteData
          }
      }, () => {
          // 父组件更新数据
          this.$emit('update');
      }, '', true);

      // 更新本地数据
      this.isArr = isArr;
      this.listData = listData;
      // 更新iscCheckAll
      this.iscCheckAll = !listData.length ? false : listData.every(item => item === true);
    },
    makeSure() {
      if (this.isArr.every((item)=> item === false)) {
        this.toast('未选择选项');
        return
      }
      this.modal.confirm(
        {
          message: "确认不通知?",
          okTitle: "确认",
          cancelTitle: "取消"
        },
        value => {
          if (value === "确认") {
            // 删除选项数据
            this.deleteListData();
          }
        }
      );
    },
    headerEvent(res) {
      this.$emit('headerEvent', res);
    }
  },
  created() {
    // 生成用于判断的数据
    select.initData.call(this);
    this.dh = this.getScreenHeight();
  }
};
</script>

<style scoped>
.list {
  justify-content: space-between;
  align-content: flex-start;
  height: 118px;
  padding: 18px 28px;
  background-color: #fff;
  border-bottom-width: 2px;
  border-bottom-style: solid;
  border-bottom-color: rgba(230, 230, 230, 1);
}
.list:active {
  background-color: #f1f1f1;
}
.footer_left:active {
  background-color: #f1f1f1;
}
.list_top,
.list_bottom {
  flex-direction: row;
  justify-content: space-between;
  width: 694px;
}
.list_last {
  height: 116px;
  background-color: #fff;
}
.list_option {
  width: 40px;
  height: 40px;
  margin-left: 30px;
  margin-right: 20px;
  border-radius: 20px;
  border-width: 2px;
  border-style: solid;
  border-color: rgba(102, 102, 102, 1);
}
.checked {
  border-color: transparent;
  background-color: #0077e2;
}
.footer {
  flex-direction: row;
  width: 750px;
  height: 92px;
}
.footer_left,
.footer_right {
  flex: 1;
}
.footer_left {
  flex-direction: row;
  align-items: center;
  border-top-width: 2px;
  border-top-style: solid;
  border-top-color: rgba(204, 204, 204, 1);
}
.footer_right {
  background-color: #0077e2;
}
</style>
