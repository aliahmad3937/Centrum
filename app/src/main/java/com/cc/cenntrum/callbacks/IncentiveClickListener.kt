package com.cc.cenntrum.callbacks

import com.cc.cenntrum.models.IncentiveResponse

interface IncentiveClickListener {
    fun onIncentiveClick(item: IncentiveResponse.Data)
}