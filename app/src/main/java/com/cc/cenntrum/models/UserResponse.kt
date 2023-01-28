package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()
){
    data class Data (
        @SerializedName("id"                ) var id               : Int?            = null,
        @SerializedName("firstname"         ) var firstname        : String?         = null,
        @SerializedName("lastname"          ) var lastname         : String?         = null,
        @SerializedName("email"             ) var email            : String?         = null,
        @SerializedName("dob"               ) var dob              : String?         = null,
        @SerializedName("gender"            ) var gender           : String?         = null,
        @SerializedName("phone"             ) var phone            : String?         = null,
        @SerializedName("country"           ) var country          : String?         = null,
        @SerializedName("pic"               ) var pic              : String?         = null,
        @SerializedName("total_time"        ) var totalTime        : String?         = null,
        @SerializedName("total_points"      ) var totalPoints      : Int?            = null,
        @SerializedName("exchange_points"   ) var exchangePoints   : Int?            = null,
        @SerializedName("user_subscription" ) var userSubscription : Int?            = null,
        @SerializedName("status"            ) var status           : Int?            = null,
        @SerializedName("device_token"      ) var deviceToken      : String?         = null,
        @SerializedName("lat"               ) var lat              : String?         = null,
        @SerializedName("lng"               ) var lng              : String?         = null,
        @SerializedName("created_at"        ) var createdAt        : String?         = null,
        @SerializedName("updated_at"        ) var updatedAt        : String?         = null,
        @SerializedName("earning_point"     ) var earningPoint     : Int?            = null,
        @SerializedName("user_rank"         ) var userRank         : String?         = null,
        @SerializedName("point_time"        ) var pointTime        : String?         = null,
        @SerializedName("champion_detail"   ) var championDetail   : ChampionDetail? = ChampionDetail(),

    )

    data class ChampionDetail (
        @SerializedName("user_name" ) var userName : String? = null,
        @SerializedName("country"   ) var country  : String? = null,
        @SerializedName("points"    ) var points   : String? = null
    )
}


