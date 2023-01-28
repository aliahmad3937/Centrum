package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class PurchaseRequest(
    @SerializedName("user_id" ) var userId : Int?    = null,
    @SerializedName("pkg_id"  ) var pkgId  : String? = null
)
