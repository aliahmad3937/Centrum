package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class IncentiveRequest(
    @SerializedName("user_id" ) var userId : Int?    = null,
    @SerializedName("lat"     ) var lat    : String? = null,
    @SerializedName("lng"     ) var lng    : String? = null
)
