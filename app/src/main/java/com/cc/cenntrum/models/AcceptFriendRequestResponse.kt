package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class AcceptFriendRequestResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data()
) {
    data class Data(

        @SerializedName("id") var id: Int? = null,
        @SerializedName("user_id") var userId: Int? = null,
        @SerializedName("f_id") var fId: Int? = null,
        @SerializedName("active") var active: Int? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null

    )
}