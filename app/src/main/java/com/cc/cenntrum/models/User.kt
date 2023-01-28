package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id"   ) var userid : Int? = null,
    @SerializedName("firstname" ) var firstname : String? = null,
    @SerializedName("lastname"  ) var lastname  : String? = null,
    @SerializedName("email"     ) var email     : String? = null,
    @SerializedName("dob"       ) var dob       : String? = null,
    @SerializedName("gender"    ) var gender    : String? = null,
    @SerializedName("phone"     ) var phone     : String? = null,
    @SerializedName("password"  ) var password  : String? = null,
    @SerializedName("country"  ) var country  : String? = null,
    @SerializedName("device_token") var token  : String? = null,
    @SerializedName("pic"       ) var pic  : String? = null,
)
