package com.byteapps.voxcii.Authentication.GoogleAuthentication

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoogleSignInViewModel(private val googleAuthRepo: GoogleAuthRepo):ViewModel() {


    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()


   fun currentUser() = googleAuthRepo.getCurrentUser()

    fun onSignInResult(intent: Intent){

        viewModelScope.launch {

            val signInResult = googleAuthRepo.signInWithIntent(intent)

            signInResult.let {result->
                _state.update {
                    it.copy(
                        isSignInSuccessful = result.status != null,
                        signInError = result.errorMessage
                    )
                }
            }

        }

    }

    suspend fun startSignIn() = googleAuthRepo.signIn()

    fun verifyUser(userId:String) = googleAuthRepo.verifyUser(userId)


}