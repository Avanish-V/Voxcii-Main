package com.byteapps.voxcii.Authentication.GoogleAuthentication

import android.content.Intent
import android.content.IntentSender
import kotlinx.coroutines.flow.Flow

interface GoogleAuthRepo {

    suspend fun signIn():IntentSender?

    suspend fun signInWithIntent(intent:Intent):SignInResult

    fun getCurrentUser():Boolean

    fun verifyUser(userId:String): Flow<UserResponse>

}