package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class MyFocusResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()
){
    data class Data (

        @SerializedName("1" ) var driving : MyFocus? = MyFocus(),
        @SerializedName("2" ) var studying : MyFocus? = MyFocus(),
        @SerializedName("3" ) var working: MyFocus? = MyFocus(),
        @SerializedName("4" ) var friends: MyFocus? = MyFocus(),
        @SerializedName("5" ) var exercise: MyFocus? = MyFocus(),
        @SerializedName("6" ) var others: MyFocus? = MyFocus()

    )

    data class MyFocus(
        @SerializedName("time" ) var time : Int?    = null,
        @SerializedName("per"  ) var per  : String? = null

    )
}
