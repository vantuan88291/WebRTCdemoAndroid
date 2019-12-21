package com.tuan88291.webrtcdemo.data.remote.service

interface iServiceApi {
    fun <S> createService(serviceClass: Class<S>): S
    fun <S> createServiceToken(serviceClass: Class<S>): S
}