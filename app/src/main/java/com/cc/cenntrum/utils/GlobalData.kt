package com.cc.cenntrum.utils

import com.cc.cenntrum.models.IncentiveResponse
import com.cc.cenntrum.models.MyFocusResponse
import com.cc.cenntrum.models.RewardResponse
import com.cc.cenntrum.models.UserResponse

object GlobalData {

    var user: UserResponse.Data? = null
    var incentiveResponse: ArrayList<IncentiveResponse.Data>? = null
    var rewardResponse: ArrayList<RewardResponse.Data>? = null
    var myFocusResponse: MyFocusResponse? = null
   lateinit var typeStartTime: String
   lateinit var pointsStartTime: String
}