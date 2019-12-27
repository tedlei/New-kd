import audioObj from './audioControl'

const Mixins = {};
let headerNav = require('../component/component_lzj/header.vue');
let footerNav = require('../component/component_lzj/footer.vue');
let loadingNav = require('../component/component_lzj/loading.vue');
Mixins.install = (Vue, options) => {
    Vue.mixin({
        components: { headerNav, footerNav, loadingNav },
        data() {
            return {
                modal: {},
                navigator: {},
                beginDate: 0,  // 用于判断当前页连续点击返回键的时间间隔
                pushBeginDate: 0    // 记录连续点击跳转时间
            }
		},
        methods: {
            toast(msg) {
                if (this.modal && this.modal.toast)
                    this.modal.toast({ message: msg })
            },
            alert(msg) {
                this.modal.alert({ message: msg })
            },
            // 跳转
            push(url, param) {
                let date = new Date(),
                    currentDate = this.pushBeginDate;
                // 阻止连续点击
                if ((date - currentDate) < 1000) return;
                this.pushBeginDate = date;

                url = 'root:' + url + '.js';
                if (!param) this.navigator.push(url);
                else this.navigator.pushParam(url, param)
            },
            // 返回
            pop(param) {
                if (param) this.navigator.backFull(param, false);
                else this.navigator.back()
            },
            // 获取页面传递的参数
            getParam() {
                let globalEvent = weex.requireModule('globalEvent');
                return new Promise(resolve => {
                    globalEvent.addEventListener("onPageInit", (e) => {
                        resolve(this.navigator.param());
                    });
                })
            },
            // 获取设备高度
            getScreenHeight() {
                return 750 / weex.config.env.deviceWidth * weex.config.env.deviceHeight
            },
            // 控制台打印
            log(msg, level) {
                if (!level) {
                    level = 'info'
                }
                let type = typeof msg;
                let log = weex.requireModule('log');
                if (type == 'object') {
                    msg = JSON.stringify(msg)
                }
                log.log({ msg: this.timestamp() + '    ' + msg + '', level: level })
            },
            /**
             * 网络请求
             * @param params    {url, body} String Object
             * @param successBack   成功回调
             * @param failBack  错误回调
             * @param isIntercept   boole   是否自己验证结果
             * @returns {Promise<void>}
             */
            async fetch(params, successBack, failBack, isIntercept, noToken) {
                let net = weex.requireModule('net'),
                    baseUrl = await this.getBaseUrl(),
                    header = {},
                    url = params.url;

                // 判断url是否以 / 开头
                if (/^\//.test(url)) url = url.slice(1);

                let token = await new Promise(resolve => {
                    this.getItem('token', (result) => {
                        resolve(result.data)
                    });
                });
                // 请求头
                if (!noToken) {
                    if (token) header.token = token;
                }

                if (!failBack) failBack = () => { };
                // 开始时的回调
                function start() { }
                // 完成时的必走的回调
                function complete(res) {}
                // 200以外的拦截处理
                let success = (result) => {
                    if (result.res.code !== 200 && !isIntercept) {
                        this.toast('登陆过期,请重新登陆');
                        this.quit()
                    } else successBack(result)
                };

                // failBack
                let fail = (result) => {
                    if (!isIntercept) {
                        this.toast('系统错误, 请重新登陆');
                        this.quit();
                    }
                    failBack.call(this, result);
                };

                net.postJson(baseUrl + url,
                    params.body,
                    header,
                    start,
                    success,
                    complete,
                    fail);
            },
            // 本地存储 用于存储用户数据
            setItem(key, value) {
                if (typeof value === 'object') {
                    value = JSON.stringify(value)
                }
                this.storage.setItem(key, value, () => {})
            },
            // 本地获取
            getItem(key, callBack) {
                this.storage.getItem(key, (event) => {
                    let data = event.data;
                    if (/^[\{ | \[]/.test(data)) {
                        event.data = JSON.parse(data)
                    }
                    callBack(event)
                });
            },
            // 深度复制
            stringify(obj) {
                return JSON.parse(JSON.stringify(obj));
            },
            // 获取当前时间
            timestamp() {
                let date = new Date();//时间戳为10位需*1000，时间戳为13位的话不需乘1000
                let Y = date.getFullYear() + '-';
                let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
                let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
                let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
                let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
                let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                return Y + M + D + h + m + s;
            },
            // 判断身份 true 为代办点   false为快递员
            verdictId() {
                return new Promise(resolve => {
                    this.getItem('userInfo', (result) => {
                        let id = result.data.userRole === '2';
                        resolve(id);
                    })
                });
            },
            /**
             * 全局注册 android 页面显示的时候触发 一般用于子页面数据更改后 返回父级页面时更新父级页面数据
             * @param callback  需要获取初始化数据的函数 返回结果为需要更新的值
             * @param changeState   需要更新的状态名
             */
            onResume(callback, changeState) {
                let globalEvent = weex.requireModule('globalEvent');
                return new Promise(resolve => {
                    globalEvent.addEventListener("onResume", async () => {
                        this[changeState] = await callback.call(this);
                        resolve(await callback.call(this));
                    });
                })
            },
            // 退出登陆
            quit() {
                // 调用退出接口
                this.fetch({
                    url: 'orderUser/loginout',
                },()=>{}, '', '', true);
                // 退出成功 清楚本地所有缓存
                this.storage.getAllKeys(result=>{
                    let data = result.data;
                    for (let i = 0, len = data.length; i < len; i++) {
                        this.storage.removeItem(data[i]);
                    }
                });
                this.push('pages/page_lm/login/login');
            },
            // 音频相关
            registerAudio() {
                audioObj.init.call(this)
            },
            //提示音频播放
            audio(value, kind) {
                audioObj.audio.call(this, value, kind)
            },
            // android 跳转到原生页面
            pushAndroid(apiName, params){
                // 获取用户信息
                this.getItem('userInfo', result=>{
                    let user = result.data;
                    if (user.userRole === '1' && apiName === 'openExScanerActivity'){
                        params.push(user.userCompany);
                    }
                    let  ScanerModule = weex.requireModule('ScanerModule');
                    ScanerModule[apiName](...params);
                });
            },
            // 获取请求地址
            getBaseUrl(){
                return new Promise(resolve => {
                    this.getItem('baseUrl', res => {
                        let url = 'http://39.100.11.220:8080/';
                        if (res.result === 'success') {
                            url = res.data;
                        }
                        resolve(url)
                    });
                })
            }
        },
        async created() {
            // icon
            let font = weex.requireModule("font");
            font.addFont('iconfont', 'root:font/fonts.ttf');
            this.navigator = weex.requireModule('navigator');
            this.modal = weex.requireModule('modal');
            this.storage = weex.requireModule('storage');
            let globalEvent = weex.requireModule('globalEvent');

            globalEvent.addEventListener("onPageInit", (param) => {
                let p = param;
                if (param && p.param && p.bubbles)
                    p = p.param;
                if (this.onLoad !== undefined)
                    this.onLoad(p)
            });
        }
    })
};
const install = Mixins.install;
export { install }
export default Mixins
