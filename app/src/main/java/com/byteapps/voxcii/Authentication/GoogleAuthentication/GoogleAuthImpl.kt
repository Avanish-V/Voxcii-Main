package com.byteapps.voxcii.Authentication.GoogleAuthentication

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.byteapps.voxcii.Network.UserProfile.data.MetaData
import com.byteapps.voxcii.Network.UserProfile.data.UserBasicProfileDTO
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


class GoogleAuthUiClient(private val context: Context,private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore):GoogleAuthRepo {

    private val oneTapClient:SignInClient = Identity.getSignInClient(context)

    private val auth=Firebase.auth

    override suspend fun signIn():IntentSender?{
        val result= try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null

        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent:Intent):SignInResult{

        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try {

            val user = auth.signInWithCredential(googleCredentials).await().user

            SignInResult(
                status = user.run { true },
                errorMessage = null
            )

        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
           SignInResult(
               status = null,
               errorMessage = e.message
           )
        }
    }

    private fun buildSignInRequest():BeginSignInRequest{
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("294461340505-efsev8jdhgp51e5oehf47nl97honru1p.apps.googleusercontent.com")
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signOut(){
        try {
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override fun getCurrentUser():Boolean{
        return FirebaseAuth.getInstance().currentUser?.uid != null
    }

    override fun verifyUser(userId: String): Flow<UserResponse> {
        return callbackFlow {

            firestore.collection("Users").document(firebaseAuth.currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    if (it.exists()){
                        trySend(
                            UserResponse(
                            status = true,
                            message = "User Already Exists")
                        )
                    }else{
                        firestore.collection("Users").document(firebaseAuth.currentUser!!.uid)
                            .set(

                                UserBasicProfileDTO(
                                    _id = firebaseAuth.currentUser!!.uid,
                                    userName = firebaseAuth.currentUser!!.displayName.toString(),
                                    userImage = firebaseAuth.currentUser!!.photoUrl.toString(),
                                    userEmail = firebaseAuth.currentUser!!.email.toString(),
                                    userBio = "",
                                    social = "",
                                    metaData = MetaData(
                                        isFirstUser = true
                                    ),
                                    likes = 0,
                                    interests = emptyList()
                                )

                            ).addOnSuccessListener {
                                trySend(
                                    UserResponse(
                                    status = true,
                                    message = "User Created Successfully"
                                )
                                )
                            }
                            .addOnFailureListener {
                                trySend(UserResponse(
                                    status = false,
                                    error = it.message)
                                )
                            }

                    }
                }
                .addOnFailureListener {
                    trySend(
                        UserResponse(
                        status = false,
                        error = it.message
                    )
                    )
                }

            awaitClose {
                close()
            }
        }
    }

}