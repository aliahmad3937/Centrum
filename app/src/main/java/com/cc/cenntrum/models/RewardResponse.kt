package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class RewardResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()
){
    data class Data (
        @SerializedName("id"         ) var id        : Int?    = null,
        @SerializedName("user_id"    ) var userId    : Int?    = null,
        @SerializedName("incent_id"  ) var incentId  : Int?    = null,
        @SerializedName("points"     ) var points    : Int?    = null,
        @SerializedName("date_time"  ) var dateTime  : Long?    = null,
        @SerializedName("created_at" ) var createdAt : String? = null,
        @SerializedName("updated_at" ) var updatedAt : String? = null,
        @SerializedName("inc_name"   ) var incName   : String? = null,
        @SerializedName("inc_img"    ) var incImg    : String? = null,
        @SerializedName("inc_value"  ) var incValue  : Int?    = null,
        @SerializedName("start_time" ) var startTime : Long? = null,
        @SerializedName("end_time"   ) var endTime   : Long? = null,
        @SerializedName("type"       ) var type      : Int?    = null
    )
}
