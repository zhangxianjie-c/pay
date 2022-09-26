package com.android.origin.framework.pay.alipay

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.android.origin.framework.pay.alipay.dispose.OrderInfoUtil
import com.android.origin.framework.pay.alipay.dispose.PayResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
Created by Zebra-RD张先杰 on 2022年7月27日17:26:14

Description:支付宝支付
 */
class AliPay private constructor() {
    companion object {
        private val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { AliPay() }

        @Synchronized
        fun create() = instance
    }

    var appId = ""
    var rsa2Private = ""
    fun execute(activity: Activity,orderStr:String,error:(()->Unit)? = null,success:(()->Unit)? = null) {
        GlobalScope.launch(Dispatchers.IO) {
            // PayTask 对象
            val alipay = PayTask(activity)
            // 调用授权接口，获取授权结果
            val result: Map<String, String> = alipay.payV2(orderStr, true)
            withContext(Dispatchers.Main) {
                val payResult = PayResult(result as Map<String?, String?>)
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo: String = payResult.getResult() // 同步返回需要验证的信息
                val resultStatus: String = payResult.getResultStatus()
                // 判断resultStatus 为9000则代表支付成功
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                if (TextUtils.equals(resultStatus, "9000")) success?.invoke()
                else error?.invoke()
            }
        }
    }
    fun executeTest(activity: Activity) {
        GlobalScope.launch(Dispatchers.IO) {
            val params: Map<String, String> =
                OrderInfoUtil.buildOrderParamMap(appId,
                    true)
            val orderParam: String = OrderInfoUtil.buildOrderParam(params)

            val sign: String = OrderInfoUtil.getSign(params, rsa2Private, true)
            val orderInfo = "$orderParam&$sign"
            // PayTask 对象
            val alipay = PayTask(activity)
            // 调用授权接口，获取授权结果
            val result: Map<String, String> = alipay.payV2(orderInfo, true)
            withContext(Dispatchers.Main) {
                val payResult = PayResult(result as Map<String?, String?>)
                /**
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                val resultInfo: String = payResult.getResult() // 同步返回需要验证的信息
                val resultStatus: String = payResult.getResultStatus()
                // 判断resultStatus 为9000则代表支付成功
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                if (TextUtils.equals(resultStatus, "9000"))
                    Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show()

            }
        }
    }

}