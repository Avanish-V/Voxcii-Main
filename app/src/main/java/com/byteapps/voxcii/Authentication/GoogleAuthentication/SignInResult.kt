package com.byteapps.voxcii.Authentication.GoogleAuthentication

import kotlinx.serialization.Serializable

@Serializable
data class SignInResult(
    val status:Boolean?,
    val errorMessage:String?
)

