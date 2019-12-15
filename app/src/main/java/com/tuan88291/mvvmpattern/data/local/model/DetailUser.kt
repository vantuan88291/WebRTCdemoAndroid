package com.tuan88291.mvvmpattern.data.local.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailUser {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    constructor(id: Int?, email: String?, firstName: String?, lastName: String?, avatar: String?) {
        this.id = id
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.avatar = avatar
    }
}