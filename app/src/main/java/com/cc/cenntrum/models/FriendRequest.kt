package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class FriendRequest(
    @SerializedName("user_id" ) var userID : Int? = null,
    @SerializedName("f_id"  ) var friendID  : Int? = null
)
