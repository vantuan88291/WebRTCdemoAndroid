package com.tuan88291.webrtcdemo

interface BaseContract : BaseView {
    fun onLoading()
    fun onLoadComplete()
    fun onError(mess: String)
}
