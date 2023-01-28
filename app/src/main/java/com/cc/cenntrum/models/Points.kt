package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class Points(
    @SerializedName("user_id"    ) var userId    : Int?    = null,
    @SerializedName("type"       ) var type      : Int?    = null,
    @SerializedName("start_time" ) var startTime : String? = null,
    @SerializedName("end_time"   ) var endTime   : String? = null,
    @SerializedName("points"     ) var points    : Int?    = null
)
