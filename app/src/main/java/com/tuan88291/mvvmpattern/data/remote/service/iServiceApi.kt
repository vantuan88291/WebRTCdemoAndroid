package com.tuan88291.mvvmpattern.data.remote.service

interface iServiceApi {
    fun <S> createService(serviceClass: Class<S>): S
    fun <S> createServiceToken(serviceClass: Class<S>): S
}