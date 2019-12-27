export default {
    sendSms(hint, num, data, callback){
        this.modal.confirm({
                message: hint,
                okTitle: "确认",
                cancelTitle: "取消"
            },
            value => {
                if (value === "确认") {
                    // 开启加载
                    this.$refs.load.openLoading();
                    // 发送短信
                    this.fetch({
                        url: '/manager/order/masssms',
                        body: {
                            number: num,
                            sms: data
                        }
                    }, (result)=>{
                        this.$refs.load.closeLoading();
                        callback(result)
                    }, ()=>{
                        this.$refs.load.closeLoading();
                    })
                }
            }
        );
    }
}