<template>
    <div style=""></div>
</template>
<script>
    export default {
        data() {
            return {};
        },
        mounted() {
            this.dh = this.getScreenHeight();
        },
        methods: {
            onLoad() {
                this.updateVersion();
            },
            // 检测更新
            updateVersion(){
                this.fetch({
                    url: 'orderVersions/getVersions',
                    body: {}
                }, async result=>{
                    let version = result.res.data.versions;
                    let env = weex.requireModule('env');
                    let jsVersionCode = env.jsVersionCode();

                    if (version*1 > jsVersionCode*1) {
                        let updater=weex.requireModule('updater');
                        updater.hotUpdate({
                                url: 'http://39.100.11.220/app.zip',
                                version: version,
                                mode: 0 // 0 静默更新  2 问询（逻辑需更改）
                            },
                            ()=>{}, // download start
                            (percent)=>{},  // download ...
                            ()=>{}, // download end
                            ()=>{}); // fail
                    }
                    // 检测登陆状态
                    this.isToken();
                });
            },
            // 判断token是否过期
            async isToken() {
                let url = "/orderUser/getseesion";
                let strandedInANumber = {number: 0};
                let toUrl = await new Promise( async resolve => {
                    let value = 'index';  // toUrl的值

                    // 检测本地token是否存在
                    let storageToken = await new Promise(resolve1 => {
                        this.getItem('token', res => {
                            //  本地无存储token 直接跳转到登陆页
                            if (res.result === 'success') {
                                resolve1(true)
                            } else {
                                value = 'pages/page_lm/login/login';
                                resolve1(false)
                            }
                        });
                    });
                    // 本地存在token 再请求服务器查看是否过期
                    if (storageToken) {
                        await new Promise(resolve2 => {
                            this.fetch({ url, body:{} }, result => {
                                let isExist = result.res.data;
                                // 获取滞留件数据
                                strandedInANumber.number = result.res.data1;
                                // token是否过期
                                if (isExist) resolve2(true);
                                else resolve2(value = 'pages/page_lm/login/login');
                            },() => {
                                resolve2(value = 'pages/page_lm/login/login');
                            });
                        });
                    }
                    resolve(value)
                });
                // 无token 或网络等等 均跳转到login登陆页
                if (toUrl !== 'index') {
                    this.quit();
                } else {
                    this.push(toUrl, strandedInANumber);
                }
            }
        }
    };
</script>
<style scoped>
</style>
