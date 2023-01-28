package com.cc.cenntrum.callbacks

import com.cc.cenntrum.models.FriendResponse


interface FriendCallback {
    fun onFriendClick(pos:Int, friend : FriendResponse.Data, isFriends: Boolean )
}