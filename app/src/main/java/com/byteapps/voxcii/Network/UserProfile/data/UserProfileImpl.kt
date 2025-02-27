package com.byteapps.voxcii.Network.UserProfile.data

import android.util.Log
import com.byteapps.voxcii.Network.UserProfile.domain.UserProfileRepo
import com.byteapps.voxcii.Utils.ResultState
import com.byteapps.voxcii.Utils.getAuthToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException

class UserProfileImpl(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth,private val firebaseDatabase: FirebaseDatabase):UserProfileRepo {

    override suspend fun getBaseProfile(userId:String): Flow<ResultState<UserBasicProfileDTO>> {
        return callbackFlow {
            trySend(ResultState.Loading)

            try {

                firestore.collection("Users").document(userId).get()
                    .addOnSuccessListener {
                        trySend(ResultState.Success(it.toObject(UserBasicProfileDTO::class.java)!!))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            } catch (e: IOException) {
                trySend(ResultState.Error("Network error: ${e.message}"))
            } catch (e: Exception) {
                trySend(ResultState.Error("An error occurred: ${e.message}"))
            } finally {
                awaitClose { Log.d("getBaseProfile", "Flow closed") }
            }
        }
    }

    override suspend fun getUserProfileById(channelId: String): Flow<ResultState<UserBasicProfileDTO>> {
        return callbackFlow {
            trySend(ResultState.Loading)

            try {
                if (channelId == FirebaseAuth.getInstance().uid) {
                    firebaseDatabase.getReference("ConnectionRoom")
                        .child(firebaseAuth.currentUser!!.uid)
                        .child("joinedUser")
                        .get()
                        .addOnSuccessListener { dataSnapshot ->

                            val userId = dataSnapshot.value?.toString()

                            if (!userId.isNullOrEmpty()) {

                                firestore.collection("Users").document(userId).get()
                                    .addOnSuccessListener { document ->
                                        document.toObject(UserBasicProfileDTO::class.java)?.let { profile ->
                                            trySend(ResultState.Success(profile))
                                        } ?: trySend(ResultState.Error("User profile not found"))
                                    }
                                    .addOnFailureListener { exception ->
                                        trySend(ResultState.Error(exception.message ?: "Unknown error"))
                                    }

                            } else {

                                trySend(ResultState.Error("Invalid user ID"))

                            }
                        }
                        .addOnFailureListener {

                            trySend(ResultState.Error("Failed to fetch joined user"))

                        }
                } else {

                    if (channelId.isNotEmpty()) {

                        firestore.collection("Users").document(channelId).get()
                            .addOnSuccessListener { document ->
                                document.toObject(UserBasicProfileDTO::class.java)?.let { profile ->
                                    trySend(ResultState.Success(profile))
                                } ?: trySend(ResultState.Error("User profile not found"))
                            }
                            .addOnFailureListener { exception ->
                                trySend(ResultState.Error(exception.message ?: "Unknown error"))
                            }

                    } else {

                        trySend(ResultState.Error("Invalid channel ID"))

                    }
                }

            } catch (e: IOException) {

                trySend(ResultState.Error("Network error: ${e.message}"))

            } catch (e: Exception) {

                trySend(ResultState.Error("An error occurred: ${e.message}"))

            } finally {

                awaitClose { Log.d("getBaseProfile", "Flow closed") }

            }
        }
    }

    override fun updateSocialAccounts(accounts: String): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                firestore.collection("Users").document(firebaseAuth.currentUser!!.uid).update("social",accounts)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(true))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            }catch (e:IOException){
                trySend(ResultState.Error("Check Your Network"))
            }

            awaitClose()

        }
    }

    override fun updateUserName(userName: String): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                firestore.collection("Users").document(firebaseAuth.currentUser!!.uid).update("userName",userName)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(true))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            }catch (e:IOException){
                trySend(ResultState.Error("Check Your Network"))
            }

            awaitClose()

        }
    }

    override fun updateAbout(about: String): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {


                firestore.collection("Users").document(firebaseAuth.currentUser!!.uid).update("userBio",about)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(true))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            }catch (e:IOException){
                trySend(ResultState.Error("Check Your Network"))
            }

            awaitClose()
        }
    }

    override fun updateInterests(interests: List<String>): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {


                firestore.collection("Users").document(firebaseAuth.currentUser!!.uid).update("interests",interests)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(true))
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            }catch (e:IOException){
                trySend(ResultState.Error("Check Your Network"))
            }

            awaitClose()


        }
    }

}