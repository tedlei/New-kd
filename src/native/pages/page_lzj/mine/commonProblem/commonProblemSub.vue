<template>
  <div>
    <headerNav title="解答" rightButton @headerEvent="headerEvent" :back="false"></headerNav>
    <div class="jieda">
      <text class="tit">{{problemAll[numberArr[0]].contextList[numberArr[1]].title}}</text>
      <text 
        v-for="(item, i) in problemAll[numberArr[0]].contextList[numberArr[1]].value" 
        :key="i"
        class="context">{{item}}</text>
    </div>
  </div>
</template>

<script>

export default {
  props: ['numberArr'],
  data() {
    return {
      problemAll: [
        {
          problem: '一、入库问题',
          contextList: [
            { 
              title: '怎么入库？', 
              value: ['点击入库，可以选择短信模板，开始扫描。系统自动识别' + 
                     '快递公司，订单号，客户姓名，和手机号。确认入库，且' + 
                     "一个快递就入库成功了。"]
            },
            { 
              title: '入库时录入信息错误，怎么办？', 
              value: [
                '在扫描入库时，发现信息错误，可以直接点击修改。', 
                '如果入库之后发现错误，可以再查询中扫描或输入' +
                "手机号查到这个快递进行修改。（只能修改客户姓名" + 
                "和客户电话。）"
              ] 
            },
            { 
              title: '每个件都需要扫描入库吗？', 
              value:  [
                '是的。因为每个快件都有运单号，扫描之后会自动生成货' +
                '号，可以对快递进行通知，出库等操作。'
              ]
            },
            { 
              title: '一边入库一边发短信吗？',
              value: [
                '可以一边入库一边发短信，也可以选择入库时不发短信，' + 
                '到库存通知里面发短信。'
              ]
            },
            { 
              title: '一个快递出库了，还能再入库吗？',
              value: [
                '同一快递出库了，可以再次入库，但绑定了快递员信息快' +
                '递，第一次出库就会以结束状态结束，第二次入库不会再'
              ]
            },
            { 
              title: '入库时手机有*号能识别吗？',
              value: [
                '不能识别。手机有*号的只能手动输入。'
              ]
            },
            { 
              title: '入库信息是否与快递公司物流信息同步？',
              value: [
                '不是的。因为，目前收发裹驿站还没有与快递公司合作。'
              ]
            }
          ]
        },
        {
          problem: '二、出库问题',
          contextList: [
            { 
              title: '怎么出库？',
              value: [
                '如果客户来取件，需要出库，点击出库，在出库中扫描快' +
                '递，也可以动手输入姓名，手机号，货号和取件号。',
                '若客户直接告知取件号，可直接到仓库里根据取件号找到' + 
                '对应的快递，扫描后直接出库。'
              ]
            },
            { 
              title: '快件取走，忘记出库，怎么查看在库列表？',
              value: [
                '可以在查询中根据运单号，手机号，姓名，货号查询并' +
                '出库。可以在快件历史中筛选出来。'
              ]
            },
            { 
              title: '什么是在库时间？',
              value: [
                '在库时间是从成功入库开始计时，在库时间如有48小时，' + 
                '系统将会生成滞留件，提示代办点或快递员。如果在库时' +
                '间超过168小时，会提示代办点是否做退件处理。'
              ]
            }
          ]
        },
        {
          problem: '三、通知问题',
          contextList: [
            { 
              title: '通知状态怎么查？',
              value: [
                '可以在短信已通知，未通知，失败短信，短信历史里面查询。'
              ] 
            },
            { 
              title: '发送记录保存多久？',
              value: [
                '六个月，因为系统只保留最新六个月的数据。'
              ] 
            },
            { 
              title: '未通知的数据会自动删除吗？',
              value: [
                '会的。因为系统只保留最新六个月的数据。'
              ] 
            }
          ]
        },
        {
          problem: '四、快件历史问题',
          contextList: [
            { 
              title: '在哪里可以查看详细的入库记录？',
              value: [
                '快件历史里面就有详细的快件记录。可以通过时间、快递' +
                '公司、快递状态筛选快件。']
            }
          ]
        },
        {
          problem: '五、滞留件问题',
          contextList: [
            { 
              title: '什么是滞留件？',
              value: [
                '滞留件是快件到达代办点，扫描入库成功开始计时，在库' +
                '时间超过48小时，就算是滞留件，系统将会提示。软件30' +
                '分钟进行一次筛选。30分钟统计一次滞留件并提示最新滞' +
                '留件。'
              ]
            },
            { 
              title: '滞留件有什么作用？',
              value: [
                '滞留件是为了提醒代办点和快递员，再次通知客户来领取' +
                '快件。如果在库时间超过168小时，会提示代办点是否做' +
                '退件处理。'
              ]
            },
          ]
        },
        {
          problem: '六、退件处理问题',
          contextList: [
            { 
              title: '什么是退件处理？',
              value: [
                '退件处理是快件出现异常，需要从代办点退给快递员。异' +
                '常情况有错放件，滞留件和快件本身问题等。'
              ]
            }
          ]
        },
        {
          problem: '七、短信模板问题',
          contextList: [
            { 
              title: '如何设置短信模板？',
              value: [
                '系统有默认的短信模板。自定义模板，先选择模板类型，' +
                '再输入短信内容。提交之后，系统会经过审核。审核成功' +
                '后可以使用。'
              ]
            }
          ]
        },
        {
          problem: '八、集件包裹问题',
          contextList: [
            { 
              title: '什么是集件包裹？',
              value: [
                '集件包裹是在快递和代办点一起使用的情况下形成的。',

                '集件包裹是快递员扫描是，系统根据快递员选择的放置地' +
                '址，将同一个地址的快件打一个虚拟包。在扫描结束后' +
                '20分钟集包结束。',

                '快递员将包裹拿到代办点时。代办点入库时，扫到集包的' +
                '快件，货号后面将出现集件包裹数的总数，每扫一个，数' +
                '量逐个减少。结束入库后将会跳转到入库快件记录页面，' +
                '入库快件记录，主要是记录异常和集包缺少件。'
              ]
            },
            { 
              title: '什么是错放件?',
              value: [
                '错放件是快递员将两个不同地址并集过包的快件，错放到' +
                '其他也有用收发裹驿站的地方。'
              ]
            },
            { 
              title: '什么是缺少件？',
              value: [
                '缺少件是快递员集过包，但是代办点没有扫描到的包裹。'
              ]
            }
          ]
        }
      ]
    };
  },
  methods: {
    headerEvent(val){this.$emit('headerEvent')}
  }
};
</script>

<style scoped>
.jieda{
  padding-left: 30px;
  padding-right: 30px;
}
.tit{
  height: 100px;
  border-bottom-width: 1px;
  border-bottom-style: solid;
  border-bottom-color: #ccc;
  color: #333;
  font-size: 32px;
  line-height: 100px;
}
.context{
  margin-top: 30px;
  font-size: 28px;
  color: #333;
}
</style>
