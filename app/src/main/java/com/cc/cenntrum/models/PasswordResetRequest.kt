package com.cc.cenntrum.models



data class PasswordResetRequest(
    val new_password: String,
    val password: String,
    val user_id: Int
)