package com.byteapps.voxcii.Network.Chats.data

data class UserChatsDTO(
    val receiverId: String = "",
    val userName:String = "",
    val userImage:String = "",
    val lastMessage:LastMessage
)
data class LastMessage(
    val lastMessage:String = "",
    val timeStamp:Long = 0
)
