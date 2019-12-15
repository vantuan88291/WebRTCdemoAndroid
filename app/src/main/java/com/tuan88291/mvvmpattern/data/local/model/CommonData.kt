package com.tuan88291.mvvmpattern.data.local.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonData<T: Any> {
    @SerializedName("data")
    @Expose
    var data: T? = null
    @SerializedName("status_code")
    @Expose
    var code: Int = 0
    @SerializedName("message")
    @Expose
    lateinit var message: String
}
