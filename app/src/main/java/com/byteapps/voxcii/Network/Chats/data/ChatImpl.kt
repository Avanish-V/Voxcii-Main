package com.byteapps.voxcii.Network.Chats.data

import android.util.Log
import com.byteapps.voxcii.Agora.User
import com.byteapps.voxcii.Network.Chats.domain.ChatRepository
import com.byteapps.voxcii.Network.UserProfile.data.UserBasicProfileDTO
import com.byteapps.voxcii.Utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ChatImpl(private val database: FirebaseDatabase, private val auth: FirebaseAuth,private val firestore: FirebaseFirestore) : ChatRepository {

    override fun sendMessage(message: String, receiverId: String, senderId: String): Flow<ResultState<Boolean>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            val sender = senderId+receiverId
            val receiver = receiverId+senderId
            val timeStamp = System.currentTimeMillis().toString()
            val key = database.getReference().push().key

            database.getReference("ChatRoom").child(senderId + receiverId)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                database.getReference("ChatRoom").child(sender)
                                    .child("messages").child(key!!)
                                    .setValue(
                                        mapOf(
                                            "message" to message,
                                            "messageId" to key,
                                            "timestamp" to timeStamp,
                                            "user" to auth.currentUser!!.uid
                                        ),

                                    ).addOnSuccessListener {
                                        database.getReference("ChatRoom").child(receiver)
                                            .child("messages").child(key)
                                            .setValue(
                                                mapOf(
                                                    "message" to message,
                                                    "messageId" to key,
                                                    "timestamp" to timeStamp,
                                                    "user" to auth.currentUser!!.uid
                                                ),

                                            ).addOnSuccessListener {
                                                trySend(ResultState.Success(true))
                                            }.addOnFailureListener {
                                                trySend(
                                                    ResultState.Error(
                                                        it.message ?: "Something went wrong"
                                                    )
                                                )
                                            }
                                    }.addOnFailureListener {
                                        trySend(
                                            ResultState.Error(
                                                it.message ?: "Something went wrong"
                                            )
                                        )
                                    }

                            } else {


                                database.getReference().child("Chats").child(senderId).child(receiverId).setValue(
                                    mapOf(
                                        "userId" to receiverId,
                                        "roomId" to senderId + receiverId,
                                        "createdAt" to timeStamp
                                    )
                                ).addOnSuccessListener {
                                    database.getReference().child("Chats").child(receiverId).child(senderId).setValue(
                                        mapOf(
                                            "userId" to senderId,
                                            "roomId" to senderId + receiverId,
                                            "createdAt" to timeStamp
                                        )
                                    ).addOnSuccessListener {

                                        database.getReference("ChatRoom").child(sender)
                                            .setValue(
                                                mapOf(
                                                    "messages" to listOf(
                                                        mapOf(
                                                            "message" to message,
                                                            "messageId" to key,
                                                            "timestamp" to timeStamp,
                                                            "user" to auth.currentUser!!.uid
                                                        )
                                                    ),
                                                    "isSeen" to false,
                                                    "isActive" to false,
                                                    "isTyping" to false
                                                )
                                            ).addOnSuccessListener {

                                                database.getReference("ChatRoom").child(receiver)
                                                    .setValue(
                                                        mapOf(
                                                            "messages" to listOf(
                                                                mapOf(
                                                                    "message" to message,
                                                                    "messageId" to key,
                                                                    "timestamp" to timeStamp,
                                                                    "user" to auth.currentUser!!.uid
                                                                )
                                                            ),
                                                            "isSeen" to false,
                                                            "isActive" to false,
                                                            "isTyping" to false
                                                        )
                                                    ).addOnSuccessListener {

                                                        trySend(ResultState.Success(true))
                                                    }.addOnFailureListener {
                                                        trySend(
                                                            ResultState.Error(
                                                                it.message ?: "Something went wrong"
                                                            )
                                                        )

                                                    }
                                            }.addOnFailureListener {
                                                trySend(
                                                    ResultState.Error(
                                                        it.message ?: "Something went wrong"
                                                    )
                                                )

                                            }


                                    }
                                }.addOnFailureListener {

                                }


                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    }

                )

            awaitClose { close() }


        }
    }

    override fun updateIsUserActive(isActive: Boolean, senderId: String, receiverId: String) {

        database.getReference("ChatRoom").child(senderId + receiverId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            database.getReference("ChatRoom").child(senderId + receiverId)
                                .updateChildren(
                                    mapOf(
                                        "isActive" to isActive,
                                    )
                                )

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }

            )

    }

    override fun updateIsUserTyping(isActive: Boolean, senderId: String, receiverId: String) {

        database.getReference("ChatRoom").child(senderId + receiverId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            database.getReference("ChatRoom").child(senderId + receiverId)
                                .updateChildren(
                                    mapOf(
                                        "isTyping" to isActive,
                                    )
                                )

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }

            )


    }

    override fun updateIsSeen(isSeen: Boolean, senderId: String, receiverId: String) {

        database.getReference("ChatRoom").child(senderId + receiverId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            database.getReference("ChatRoom").child(senderId + receiverId)
                                .updateChildren(
                                    mapOf(
                                        "isSeen" to isSeen,
                                    )
                                )

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }

            )
    }

    override fun getIsUserActive(senderId: String, receiverId: String): Flow<Boolean> {
        return callbackFlow {
            database.getReference("ChatRoom").child(receiverId+senderId)
                .child("isActive")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()){
                            trySend(snapshot.value as Boolean)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            awaitClose { close() }
        }
    }

    override fun getIsUserTyping(senderId: String, receiverId: String): Flow<Boolean> {
        return callbackFlow {
            database.getReference("ChatRoom").child(receiverId + senderId)
                .child("isTyping")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()){
                            trySend(snapshot.value as Boolean)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            awaitClose { close() }
        }
    }

    override fun getIsSeen(senderId: String, receiverId: String): Flow<Boolean> {
        return callbackFlow {
            database.getReference("ChatRoom").child(receiverId + senderId)
                .child("isSeen")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            trySend(snapshot.value as Boolean)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            awaitClose { close() }
        }
    }

    override fun receiveMessage(senderId: String, receiverId: String): Flow<ResultState<List<ReceiveMessageDTO>>> {
        return callbackFlow {

            trySend(ResultState.Loading)

            try {
                database.getReference("ChatRoom").child(senderId + receiverId)
                    .child("messages") // Ensure we're inside "messages"
                    .addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val messageList = mutableListOf<ReceiveMessageDTO>()

                                    snapshot.children.forEach { messageSnapshot ->
                                        // Ensure data is in correct format before conversion
                                        val messageData = messageSnapshot.getValue(ReceiveMessageDTO::class.java)
                                        if (messageData != null) {
                                            messageList.add(messageData)
                                        } else {
                                            Log.e("ChatImpl", "Invalid message format: ${messageSnapshot.value}")
                                        }
                                    }

                                    trySend(ResultState.Success(messageList))

                                } else {
                                    trySend(ResultState.Error("No messages found"))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                trySend(ResultState.Error(error.message))
                            }
                        }
                    )
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message ?: "Unknown error"))
            }

            awaitClose { close() }
        }
    }

    override fun getChats(): Flow<ResultState<List<UserChatsDTO>>> {
        return callbackFlow {
            trySend(ResultState.Loading)

            val currentUserId = auth.currentUser?.uid ?: return@callbackFlow

            database.getReference("Chats")
                .child(currentUserId)
                .get()
                .addOnSuccessListener { chatSnapshot ->

                    val chatList = mutableListOf<UserChatsDTO>()

                    if (!chatSnapshot.exists()) {
                        trySend(ResultState.Success(emptyList()))
                        return@addOnSuccessListener
                    }

                    val chatCount = chatSnapshot.childrenCount
                    var processedChats = 0

                    chatSnapshot.children.forEach { chat ->
                        val receiverId = chat.child("userId").value?.toString()
                        val roomId = chat.child("roomId").value?.toString()

                        if (!receiverId.isNullOrEmpty() && !roomId.isNullOrEmpty()) {
                            firestore.collection("Users").document(receiverId).get()
                                .addOnSuccessListener { userSnapshot ->
                                    val userData = userSnapshot.toObject(UserBasicProfileDTO::class.java)

                                    if (userData != null) {
                                        database.getReference("ChatRoom")
                                            .child(roomId)
                                            .child("messages")
                                            .limitToLast(1) // Get the last message
                                            .get()
                                            .addOnSuccessListener { messageSnapshot ->

                                                val lastMessage = messageSnapshot.children.firstOrNull()?.getValue(ReceiveMessageDTO::class.java)

                                                chatList.add(
                                                    UserChatsDTO(
                                                        receiverId = receiverId,
                                                        userName = userData.userName,
                                                        userImage = userData.userImage,
                                                        lastMessage = LastMessage(
                                                            lastMessage = lastMessage?.message ?: "",
                                                            timeStamp = lastMessage?.timestamp?.toLong()?:0

                                                        )
                                                    )
                                                )

                                                processedChats++
                                                if (processedChats == chatCount.toInt()) {
                                                    trySend(ResultState.Success(chatList))
                                                }
                                            }
                                    } else {
                                        processedChats++
                                        if (processedChats == chatCount.toInt()) {
                                            trySend(ResultState.Success(chatList))
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    trySend(ResultState.Error(it.message ?: "Error fetching user data"))
                                }
                        } else {
                            processedChats++
                            if (processedChats == chatCount.toInt()) {
                                trySend(ResultState.Success(chatList))
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.message ?: "Failed to fetch chats"))
                }

            awaitClose { close() }
        }
    }


}

