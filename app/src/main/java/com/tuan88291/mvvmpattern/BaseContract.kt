package com.tuan88291.mvvmpattern

interface BaseContract : BaseView {
    fun onLoading()
    fun onLoadComplete()
    fun onError(mess: String)
}
