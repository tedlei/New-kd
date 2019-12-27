package com.sam.scaner.bean

import android.support.annotation.Keep

import java.io.Serializable

@Keep
class LoginBean : Serializable {

    var code: Int = 0
    var data: DataBean? = null
    var isIsok: Boolean = false
    var message: String? = null

    class DataBean {
        /**
         * id : null
         * userPhone : 13594516029
         * userName : 彭
         * userPassword :
         * address : 重庆
         * userRole : 2
         * userCompany : 韵达
         * userMoney : null
         * userNum : 0
         * userHead : null
         * sessionId : E2CA9632A48C8BA7A94CF295E0096FC9
         */

        var id: Any? = null
        var userPhone: String? = null
        var userName: String? = null
        var userPassword: String? = null
        var address: String? = null
        var userRole: String? = null
        var userCompany: String? = null
        var userMoney: Any? = null
        var userNum: String? = null
        var userHead: Any? = null
        var sessionId: String? = null

        override fun toString(): String {
            return "DataBean{" +
                    "id=" + id +
                    ", userPhone='" + userPhone + '\''.toString() +
                    ", userName='" + userName + '\''.toString() +
                    ", userPassword='" + userPassword + '\''.toString() +
                    ", address='" + address + '\''.toString() +
                    ", userRole='" + userRole + '\''.toString() +
                    ", userCompany='" + userCompany + '\''.toString() +
                    ", userMoney=" + userMoney +
                    ", userNum='" + userNum + '\''.toString() +
                    ", userHead=" + userHead +
                    ", sessionId='" + sessionId + '\''.toString() +
                    '}'.toString()
        }
    }
}
