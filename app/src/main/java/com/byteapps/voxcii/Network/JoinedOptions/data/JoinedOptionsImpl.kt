package com.byteapps.voxcii.Network.JoinedOptions.data

import com.byteapps.voxcii.Network.JoinedOptions.domain.JoinedOptionsRepository
import com.byteapps.voxcii.Network.UserProfile.data.UserBasicProfileDTO
import com.byteapps.voxcii.Utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class JoinedOptionsImpl(private val firestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth) : JoinedOptionsRepository {

    override fun sendConnectRequest(receiverId:String,connectRequestDTO: ConnectRequestDTO): Flow<ResultState<Boolean>> = callbackFlow {

        trySend(ResultState.Loading)

        try {

            firestore.collection("Connections").document(receiverId)
                .collection("ConnectRequests")
                .document(firebaseAuth.currentUser?.uid.toString())
                .set(connectRequestDTO)
                .addOnSuccessListener {
                    trySend(ResultState.Success(true))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.message.toString()))
                }

        }catch (e:Exception){
            trySend(ResultState.Error(e.message.toString()))
        }

        awaitClose { close() }

    }

    override fun sendLike(receiverId: String,currentLikesCount:Int,isLike:Boolean): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                if (isLike){
                    firestore.collection("Users").document(receiverId)
                        .update("likes",currentLikesCount+1)
                        .addOnSuccessListener {
                            trySend(ResultState.Success(true))
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.message.toString()))
                        }
                }else{
                    firestore.collection("Users").document(receiverId)
                        .update("likes",currentLikesCount-1)
                        .addOnSuccessListener {
                            trySend(ResultState.Success(true))
                        }.addOnFailureListener {
                            trySend(ResultState.Error(it.message.toString()))
                        }
                }



            }catch (e:Exception){

            }

            awaitClose { close() }

        }
    }

    override fun getNotifications(): Flow<ResultState<List<RequestNotificationDTO>>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                firestore.collection("Connections").document(firebaseAuth.currentUser!!.uid)
                    .collection("ConnectRequests")
                    .whereEqualTo("status",false)
                    .get() // ✅ Fetch all documents instead of just one
                    .addOnSuccessListener { snapshot ->

                        val connectionRequestList = mutableListOf<RequestNotificationDTO>()

                        for (document in snapshot.documents) { // ✅ Loop through all requests
                            val request = document.toObject(ConnectRequestDTO::class.java)

                            request?.let { requestData ->
                                firestore.collection("Users").document(requestData.senderId)
                                    .get()
                                    .addOnSuccessListener { userSnapshot ->
                                        val userData = userSnapshot.toObject(UserBasicProfileDTO::class.java)

                                        if (userData != null) {
                                            connectionRequestList.add(
                                                RequestNotificationDTO(
                                                    userName = userData.userName,
                                                    userImage = userData.userImage,
                                                    userBio = userData.userBio,
                                                    senderId = userData._id,
                                                    status = requestData.status
                                                )
                                            )
                                        }

                                        // ✅ Ensure results are sent only after processing all users
                                        if (connectionRequestList.size == snapshot.documents.size) {
                                            trySend(ResultState.Success(connectionRequestList))
                                        }

                                    }.addOnFailureListener {
                                        trySend(ResultState.Error(it.message.toString()))
                                    }
                            }
                        }

                        if (snapshot.isEmpty) {
                            trySend(ResultState.Success(emptyList())) // ✅ Return empty list if no requests found
                        }

                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }


            }catch (e:Exception){
                trySend(ResultState.Error(e.message.toString()))
            }

            awaitClose { close() }

        }
    }

    override suspend fun acceptConnectionRequest(senderId: String): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                firestore.collection("Connections").document(firebaseAuth.currentUser!!.uid)
                    .collection("ConnectRequests")
                    .document(senderId)
                    .update("status",true)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(true))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            }catch (e:Exception){

                trySend(ResultState.Error(e.message.toString()))


            }

            awaitClose { close() }

        }
    }

    override suspend fun rejectConnectionRequest(senderId: String): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                firestore.collection("Connections").document(firebaseAuth.currentUser!!.uid)
                    .collection("ConnectRequests")
                    .document(senderId)
                    .delete()
                    .addOnSuccessListener {
                        trySend(ResultState.Success(true))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }


            }catch (e:Exception){

                trySend(ResultState.Error(e.message.toString()))

            }

            awaitClose { close() }

        }
    }

    override fun getConnectionsCount(): Flow<ResultState<Int>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {
                firestore.collection("Connections").document(firebaseAuth.currentUser!!.uid)
                    .collection("ConnectRequests")
                    .whereEqualTo("status",true)
                    .count()
                    .get(AggregateSource.SERVER)
                    .addOnSuccessListener {
                        trySend(ResultState.Success(it.count.toInt()))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }

            }catch (e:Exception){
                trySend(ResultState.Error(e.message.toString()))
            }

            awaitClose { close() }

        }
    }

    override fun getConnections(): Flow<ResultState<List<RequestNotificationDTO>>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {

                firestore.collection("Connections").document(firebaseAuth.currentUser!!.uid)
                    .collection("ConnectRequests")
                    .whereEqualTo("status",true)
                    .get() // ✅ Fetch all documents instead of just one
                    .addOnSuccessListener { snapshot ->

                        val connectionRequestList = mutableListOf<RequestNotificationDTO>()

                        for (document in snapshot.documents) { // ✅ Loop through all requests
                            val request = document.toObject(ConnectRequestDTO::class.java)

                            request?.let { requestData ->
                                firestore.collection("Users").document(requestData.senderId)
                                    .get()
                                    .addOnSuccessListener { userSnapshot ->
                                        val userData = userSnapshot.toObject(UserBasicProfileDTO::class.java)

                                        if (userData != null) {
                                            connectionRequestList.add(
                                                RequestNotificationDTO(
                                                    userName = userData.userName,
                                                    userImage = userData.userImage,
                                                    senderId = userData._id,
                                                    status = requestData.status,
                                                    userBio = userData.userBio
                                                )
                                            )
                                        }

                                        // ✅ Ensure results are sent only after processing all users
                                        if (connectionRequestList.size == snapshot.documents.size) {
                                            trySend(ResultState.Success(connectionRequestList))
                                        }

                                    }.addOnFailureListener {
                                        trySend(ResultState.Error(it.message.toString()))
                                    }
                            }
                        }

                        if (snapshot.isEmpty) {
                            trySend(ResultState.Success(emptyList())) // ✅ Return empty list if no requests found
                        }

                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.message.toString()))
                    }


            }catch (e:Exception){
                trySend(ResultState.Error(e.message.toString()))
            }

            awaitClose { close() }

        }
    }


}