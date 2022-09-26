package com.android.origin.framework.pay

import com.android.origin.framework.pay.alipay.AliPay
import com.android.origin.framework.pay.vxpay.VxPay

/**
Created by Zebra-RD张先杰 on 2022年7月27日17:23:39

Description:支付模块
 */

class Pay  private constructor() {
    companion object {
        private val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Pay() }

        @Synchronized
        fun create() = instance
    }

    fun aliPay(init: AliPay.() -> Unit) {
        val pay = AliPay.create()
        init.invoke(pay)
    }
    fun vxPay(init: VxPay.() -> Unit) {
        val pay = VxPay.create()
        init.invoke(pay)
    }
}