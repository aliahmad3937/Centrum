package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class FilterRequest(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null

)
