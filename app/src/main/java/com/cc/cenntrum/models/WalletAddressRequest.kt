package com.cc.cenntrum.models

import com.google.gson.annotations.SerializedName

data class WalletAddressRequest(
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("wallet") var wallet: ArrayList<Wallet> = arrayListOf()
) {
    data class Wallet(

        @SerializedName("inc_id") var incId: Int? = null,
        @SerializedName("address") var address: String? = null

    )
}
