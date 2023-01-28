package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class IncentiveResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
){

    data class Data (
        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("name"       ) var name      : String? = null,
        @SerializedName("value"      ) var value     : Int?    = null,
        @SerializedName("quantity"   ) var quantity  : Int?    = null,
        @SerializedName("req_point"  ) var reqPoint  : Int?    = null,
        @SerializedName("img"        ) var img       : String? = null,
        @SerializedName("lat"        ) var lat       : String? = null,
        @SerializedName("lng"        ) var lng       : String? = null,
        @SerializedName("radius"     ) var radius    : Int?    = null,
        @SerializedName("created_at" ) var createdAt : String? = null,
        @SerializedName("updated_At" ) var updatedAt : String? = null,
        @SerializedName("inc_address" ) var address : String? = null

    )
}