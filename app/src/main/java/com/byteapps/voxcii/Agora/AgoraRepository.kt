package com.byteapps.voxcii.Agora

import android.content.Context
import com.byteapps.voxcii.Agora.media.RtcConnectionStatus
import com.byteapps.voxcii.Agora.media.joiningParameters
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AgoraRepository {

    fun rtcEngineInit():Flow<ResultState<RtcConnectionStatus>>

     fun joinChannel(channelId: String,token:String,uid: Int)

    fun startSignaling(onRoomFound: (joiningParameters) -> Unit,filterList: List<String>)

    fun deleteChannelRoom(userId: String)

    fun onRightSwipe(userId:String)

    fun leaveChannel()

    fun leaveChannelWhenOffline()


    fun MicMute()

    fun MicUnMute()

    fun SpeakerMute()

    fun SpeakerUnMute()
}