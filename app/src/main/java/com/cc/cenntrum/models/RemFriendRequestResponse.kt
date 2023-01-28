package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class RemFriendRequestResponse (
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Int?     = null
)
