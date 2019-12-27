/**
 * 调用方法: import name from 'path'
 * name.phone.call(this, newValue)
 */
export default {
  // 手机验证
  phone (newValue) {
    if (/^1[3456789]\d{9}$/.test(newValue)) this.phoneCorrect = true
    else this.phoneCorrect = false
  },
  // 密码验证 6-16位
  password (newValue) {
    if (newValue.length > 5 && newValue.length < 17) this.passwordCorrect = true
    else this.passwordCorrect = false
    // if (newValue.length > 5
    //     && newValue.length < 17
    //     && /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&.*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&.*]+$)(?![\d!@#$%^&.*]+$)[a-zA-Z\d!@#$%^&.*]+$/.test(newValue)) {
    //     this.passwordCorrect = true
    // }
  },
  // 验证码1
  smsCode (newValue) {
    if (newValue.length === 6 && /^\d{6}$/.test(newValue.trim())) this.smsCodeCorrect = true
    else this.smsCodeCorrect = false
  },
  /**
* 用户姓名验证
*/
  username (newValue) {
    if (/[^\u4e00-\u9fa5]/.test(newValue) || newValue.length < 2 || newValue.length > 6) this.tNameCorrect = true
    else this.tNameCorrect = false
  },

  //替换输入框的表情包
  onInput(val){
    var regStr = /[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF][\u200D|\uFE0F]|[\uD83C|\uD83D|\uD83E][\uDC00-\uDFFF]|[0-9|*|#]\uFE0F\u20E3|[0-9|#]\u20E3|[\u203C-\u3299]\uFE0F\u200D|[\u203C-\u3299]\uFE0F|[\u2122-\u2B55]|\u303D|[\A9|\AE]\u3030|\uA9|\uAE|\u3030/gi;
    if (regStr.test(val)) {
      val = val.replace(regStr, "");
    }
    return val
  }
}
