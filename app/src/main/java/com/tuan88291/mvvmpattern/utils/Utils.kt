package com.tuan88291.mvvmpattern.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.tuan88291.mvvmpattern.utils.service.SocketService

object Utils {
    fun log(msg: String, data: Any) {
        try {
            LogUtils.e("$msg: --->" + Gson().toJson(data))
        } catch (e: Exception) {
            LogUtils.e("$msg: --->" + e.message)
        }
    }
    fun startSocketService(context: Context) {
        if (!SocketService.checkrunning) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                context.startForegroundService(Intent(context, SocketService::class.java))
            } else {
                context.startService(Intent(context, SocketService::class.java))
            }
        }
    }
}