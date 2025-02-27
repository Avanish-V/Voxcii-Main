package com.byteapps.voxcii.Authentication.GoogleAuthentication

import kotlinx.serialization.Serializable

@Serializable
data class SignInState(
    val isSignInSuccessful:Boolean = false,
    val signInError:String? = null
)
