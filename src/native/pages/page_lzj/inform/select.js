export default {
  /**
   * 初始化后用于判断的数据
   * isArr 判断数据
   * listData 渲染数据
   */
  initData () {
    this.isArr = this.listData.map(() => false)
  },
  // 单个切换
  cutStatus (i) {
    let newArr = JSON.parse(JSON.stringify(this.isArr));
    newArr[i] = !this.isArr[i];
    // 更新iscCheckAll isArr
    this.iscCheckAll = newArr.every(item => item === true);
    this.isArr = newArr
  },
  // 全选按钮
  checkAll () {
    let { isArr, iscCheckAll } = this;
    this.isArr = JSON.parse(JSON.stringify(isArr)).fill(!iscCheckAll);
    this.iscCheckAll = !iscCheckAll
  }
}
