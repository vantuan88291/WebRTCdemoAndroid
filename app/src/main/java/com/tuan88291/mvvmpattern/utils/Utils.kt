package com.tuan88291.mvvmpattern.utils

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson

object Utils {
    fun log(msg: String, data: Any) {
        try {
            LogUtils.e("$msg: --->" + Gson().toJson(data))
        } catch (e: Exception) {
            LogUtils.e("$msg: --->" + e.message)
        }
    }
}