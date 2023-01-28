package com.cc.cenntrum.callbacks

import com.cc.cenntrum.models.FriendResponse

interface NotificationCallback {

    fun onNotificationClick(pos:Int, friend:FriendResponse.Data,isAccept:Boolean)
}