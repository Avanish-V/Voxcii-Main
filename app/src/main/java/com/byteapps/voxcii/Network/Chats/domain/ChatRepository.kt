package com.byteapps.voxcii.Network.Chats.domain

import com.byteapps.voxcii.Network.Chats.data.ReceiveMessageDTO
import com.byteapps.voxcii.Network.Chats.data.UserChatsDTO
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun sendMessage(message: String, receiverId: String,senderId:String):Flow<ResultState<Boolean>>

    fun updateIsUserActive(isActive:Boolean,senderId: String,receiverId: String)

    fun updateIsUserTyping(isActive:Boolean,senderId: String,receiverId: String)

    fun updateIsSeen(isSeen:Boolean,senderId: String,receiverId: String)

    fun getIsUserActive(senderId: String,receiverId: String):Flow<Boolean>

    fun getIsUserTyping(senderId: String,receiverId: String):Flow<Boolean>

    fun getIsSeen(senderId: String,receiverId: String):Flow<Boolean>

    fun receiveMessage(senderId: String,receiverId: String):Flow<ResultState<List<ReceiveMessageDTO>>>

    fun getChats():Flow<ResultState<List<UserChatsDTO>>>


    
}