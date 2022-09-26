package com.android.origin.framework.pay.vxpay

import android.app.Activity
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory


/**
Created by Zebra-RD张先杰 on 2022年8月1日14:18:48

Description:微信支付
 */
class VxPay private constructor() {
    companion object {
        private val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { VxPay() }

        @Synchronized
        fun create() = instance
    }

    var appId = ""
    var api: IWXAPI? = null
    var resultCheck:((Boolean) ->Unit)? = null
    fun execute(activity: Activity,  request: PayReq) {
        val api = WXAPIFactory.createWXAPI(activity, request.appId, false)
        api!!.registerApp(request.appId)
        api?.sendReq(request)
    }
}