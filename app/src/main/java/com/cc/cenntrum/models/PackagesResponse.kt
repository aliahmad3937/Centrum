package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class PackagesResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
){
    data class Data (
        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("title"      ) var title     : String? = null,
        @SerializedName("price"      ) var price     : Int?    = null,
        @SerializedName("points"     ) var points    : Int?    = null,
        @SerializedName("coins"     )  var coins    : Int?    = null,
        @SerializedName("color"      ) var color     : String? = null,
        @SerializedName("created_at" ) var createdAt : String? = null,
        @SerializedName("updated_at" ) var updatedAt : String? = null

    )
}
