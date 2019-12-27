export default {
    init(num) { // 1 未通知初始化    2已通知初始化
        // 初始化当前页面数据
        let url = 'manager/order/inform';
        if (num === 2) {
            url = '/manager/order/getinform';
        }

        return new Promise(resolve => {
            this.fetch({
                url: url,
                body: {}
            }, result => {
                let res = result.res;
                if (res.code === 200) {
                    resolve(res.data)
                }
            });
        });
    },
    find(value, smsstatus){
        // 初始化当前页面数据
        return new Promise(resolve => {
            this.fetch({
                url: '/manager/order/select',
                body: {
                    company: value,
                    smsstatus
                }
            }, result => {
                resolve(result.res.data);
            });
        });
    }
}