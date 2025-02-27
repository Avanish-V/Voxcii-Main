package com.byteapps.voxcii.Utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

fun getAuthToken():String?{

    val tokenFlow = MutableStateFlow<String?>(null)

    FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result?.token
            tokenFlow.value = token
            Log.d("FIREBASE_TOKEN", "Token updated: $token")
        } else {
            Log.e("FIREBASE_TOKEN", "Failed to get token", task.exception)
        }
    }

    return tokenFlow.value
}