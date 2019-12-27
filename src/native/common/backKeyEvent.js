export default {
    androidEnableExit(callback){
        let globalEvent = weex.requireModule('globalEvent');
        //android 页面显示的时候触发
        return new Promise(resolve => {
            globalEvent.addEventListener("onResume", (param) => {
                let page = weex.requireModule('page');
                // 关闭返回键事件
                page.enableBackKey(false);
                if (!callback) {    // 连续返回退出
                    page.setBackKeyCallback(() =>{
                        let currentDate = new Date();
                        // 点击提示退出 间隔超过2s退出
                        if (currentDate - this.beginDate > 2000) {
                            this.toast("再按一次退出");
                            this.beginDate = currentDate;
                        } else page.exit()
                    });
                    resolve('默认注册连续返回退出');
                } else {
                    // 返回键自定义需求
                    resolve(page.setBackKeyCallback(callback.bind(this, page)));
                }
            });
        });
    }
}