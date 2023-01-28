package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class BuyIncentiveRequest(
    @SerializedName("user_id"         ) var id        : Int?    = null,
    @SerializedName("incent_id"       ) var incent_id      : String? = null,
)
