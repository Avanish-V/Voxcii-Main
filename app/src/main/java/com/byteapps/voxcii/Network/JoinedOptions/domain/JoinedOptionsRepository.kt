package com.byteapps.voxcii.Network.JoinedOptions.domain

import com.byteapps.voxcii.Network.JoinedOptions.data.ConnectRequestDTO
import com.byteapps.voxcii.Network.JoinedOptions.data.RequestNotificationDTO
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface JoinedOptionsRepository {

    fun sendConnectRequest(receiverId:String,connectRequestDTO: ConnectRequestDTO):Flow<ResultState<Boolean>>

    fun sendLike(receiverId: String,currentLikesCount:Int,isLike:Boolean):Flow<ResultState<Boolean>>

    fun getNotifications():Flow<ResultState<List<RequestNotificationDTO>>>

    suspend fun acceptConnectionRequest(senderId:String):Flow<ResultState<Boolean>>

    suspend fun rejectConnectionRequest(senderId:String):Flow<ResultState<Boolean>>

    fun getConnectionsCount():Flow<ResultState<Int>>

    fun getConnections():Flow<ResultState<List<RequestNotificationDTO>>>

}