package com.tuan88291.webrtcdemo.data.local.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataUser {
    @SerializedName("data")
    @Expose
    var data: MutableList<DetailUser>? = null
}
