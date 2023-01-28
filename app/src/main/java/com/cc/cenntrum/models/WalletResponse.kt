package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class WalletResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<FriendResponse.Data> = arrayListOf()
){
    data class Data (
        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("inc_name"   ) var incName   : String? = null,
        @SerializedName("inc_img"    ) var incImg    : String? = null,
        @SerializedName("inc_value"  ) var incValue  : Int?    = null
    )
}
