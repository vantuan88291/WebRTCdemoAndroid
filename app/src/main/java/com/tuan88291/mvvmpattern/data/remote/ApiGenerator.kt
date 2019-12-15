package com.tuan88291.mvvmpattern.data.remote

import com.tuan88291.mvvmpattern.data.remote.service.iServiceApi

class ApiGenerator(val service: iServiceApi) {
    fun createApi(): CallApi {
        return service.createService(CallApi::class.java)
    }

    fun createTokenApi(): CallApi {
        return service.createServiceToken(CallApi::class.java)
    }
}