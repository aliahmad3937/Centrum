package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class PointsResponse(
    @SerializedName("status"       ) var status      : Boolean?        = null,
    @SerializedName("message"      ) var message     : String?         = null,
    @SerializedName("total_points" ) var totalPoints : String?         = null,
    @SerializedName("data"         ) var data        : ArrayList<Data> = arrayListOf()

){
    data class Data (
        @SerializedName("user_id"    ) var userId    : Int?    = null,
        @SerializedName("type"       ) var type      : Int?    = null,
        @SerializedName("start_time" ) var startTime : Long? = null,
        @SerializedName("end_time"   ) var endTime   : Long? = null,
        @SerializedName("points"     ) var points    : Int?    = null,
        @SerializedName("updated_at" ) var updatedAt : String? = null,
        @SerializedName("created_at" ) var createdAt : String? = null,
        @SerializedName("id"         ) var id        : Int?    = null
    )
}
