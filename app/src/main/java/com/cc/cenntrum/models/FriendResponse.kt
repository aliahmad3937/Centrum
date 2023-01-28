package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class FriendResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf()
) {

    data class Data(
        @SerializedName("id") var id: Int? = null,
        @SerializedName("last_activity") var type: Int? = 0,
        @SerializedName("firstname") var firstname: String? = null,
        @SerializedName("lastname") var lastname: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("dob") var dob: String? = null,
        @SerializedName("gender") var gender: String? = null,
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("country") var country: String? = null,
        @SerializedName("device_token") var deviceToken: String? = null,
        @SerializedName("user_id") var userId: Int? = null,
        @SerializedName("f_id") var fId: Int? = null,
        @SerializedName("active") var active: Int? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null

    )
}
