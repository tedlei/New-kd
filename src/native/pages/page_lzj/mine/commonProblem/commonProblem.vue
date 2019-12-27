<template>
  <slider :infinite="false" :index="index" @change="change">
    <div>
      <headerNav title="常见问题" rightButton></headerNav>
      <scroller>
        <div class="wraper">
          <div v-for="(item, index) in listData" :key="index">
            <div class="list"  @click="cutOption(index)">
              <text style="color: #333;font-size: 36px;">{{item.problemContext}}</text>
              <text style="font-size: 28px;">＞</text>
            </div>
            <scroller class="scroller">
              <div v-if="isArr[index]">
                <text
                        class="problem"
                        v-for="(value, id) in item.contextList"
                        :key="id"
                        @click="wxcRadioListChecked(index, id)">{{value.title}}</text>
                <div class="problemFooter" :class="[(listData.length - 1) === index ? 'problemFooterLast': '']"></div>
              </div>
            </scroller>
          </div>
          <text>{{test}}</text>
        </div>
      </scroller>
    </div>
    <commonProblemSub v-if="numberArr.length" @headerEvent="headerEvent" :numberArr="numberArr"></commonProblemSub>
  </slider>
</template>

<script>
import { WxcRadio } from 'weex-ui'
import select from '../../inform/select'
import commonProblemSub from './commonProblemSub.vue'
import backKeyEvent from '../../../../common/backKeyEvent'

export default {
  components: {WxcRadio, commonProblemSub},
  data() {
    return {
      index: 0, // 列表与问题详情切换 0 1
      isArr: [],  // 一级列表的切换
      numberArr: [],  // 存储序号  用于详细页获取对应信息
      listData: [
        {
          problemContext: '一、入库问题',
          contextList: [
            { title: '1、怎么入库？', value: 0 },
            { title: '2、入库时录入信息错误，怎么办？', value: 0 },
            { title: '3、每个件都需要扫描入库吗？', value: 0 },
            { title: '4、一边入库一边发短信吗？', value: 0 },
            { title: '5、一个快递出库了，还能再入库吗？', value: 0 },
            { title: '6、入库时手机有*号能识别吗？', value: 0 },
            { title: '7、入库信息是否与快递公司物流信息同步？', value: 0 }
          ]
        },
        {
          problemContext: '二、出库问题',
          contextList: [
            { title: '1、怎么出库？', value: 1 },
            { title: '2、快件取走，忘记出库，怎么查看在库列表？', value: 1 },
            { title: '3、什么是在库时间？', value: 1 }
          ]
        },
        {
          problemContext: '三、通知问题',
          contextList: [
            { title: '1、通知状态怎么查？', value: 2 },
            { title: '2、发送记录保存多久？', value: 2 },
            { title: '3、未通知的数据会自动删除吗？', value: 2 }
          ]
        },
        {
          problemContext: '四、快件历史问题',
          contextList: [
            { title: '1、在哪里可以查看详细的入库记录？', value: 3 }
          ]
        },
        {
          problemContext: '五、滞留件问题',
          contextList: [
            { title: '1、什么是滞留件？', value: 4 },
            { title: '2、滞留件有什么作用？', value: 4 },
          ]
        },
        {
          problemContext: '六、退件处理问题',
          contextList: [
            { title: '1、什么是退件处理？', value: 5 }
          ]
        },
        {
          problemContext: '七、短信模板问题',
          contextList: [
            { title: '1、如何设置短信模板？', value: 6 }
          ]
        },
        {
          problemContext: '八、集件包裹问题',
          contextList: [
            { title: '1、什么是集件包裹？', value: 7 },
            { title: '2、什么是错放件?', value: 7 },
            { title: '3、什么是缺少件？', value: 7 }
          ]
        }
      ],
      test: ''
    };
  },
  methods: {
    // 一级列表切换
    cutOption(i){
      select.cutStatus.call(this, i)
    },
    // 点击问题详细
    wxcRadioListChecked(index, id){
      this.numberArr = [index, id]
      this.index = 1
    },
    // slider 序号更改触发
    change(event){
      if (event.index === 0) {
        this.index = 0;
        this.numberArr = []
      } else this.index = 1
    },
    // 详细页点击头部右上角返回
    headerEvent(){this.index = 0}
  },
  async created(e) {
    select.initData.call(this);
    await backKeyEvent.androidEnableExit.call(this, (page)=>{
        if (this.index === 1) {
            this.index = 0
        } else this.pop()
    });
  }
};
</script>

<style scoped>
.wraper{
  padding-left: 32px;
  padding-right: 30px;
}
.list{
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  height: 100px;
}
.list,
.problem{
  border-bottom-width: 1px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
}
.problem{
  font-size: 28px;
  line-height: 80px;
  color: #333;
}
.list:active{
  background-color: #e5e5e5;
}
.problem:active{
  background-color: #e5e5e5;
}
.problemFooter{
  height: 20px;
  background-color: #f2f2f2;
}
.problemFooterLast{
  background-color: transparent;
}
</style>
