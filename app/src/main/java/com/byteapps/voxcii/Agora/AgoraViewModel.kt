package com.byteapps.voxcii.Agora

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.voxcii.Agora.media.RtcConnectionStatus
import com.byteapps.voxcii.Agora.media.joiningParameters
import com.byteapps.voxcii.Utils.ResultState
import io.agora.meta.IMetaServiceEventHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AgoraViewModel( val agoraRepository: AgoraRepository):ViewModel(){

    private val _connectionState : MutableStateFlow<ConnectionResultState?> = MutableStateFlow(null)
    val connectionState: StateFlow<ConnectionResultState?> = _connectionState


    fun AgoraInit(){

        viewModelScope.launch {
            agoraRepository.rtcEngineInit().collect{
                when(it){
                    is ResultState.Loading->{
                        _connectionState.value = ConnectionResultState(isLoading = true)
                    }
                    is ResultState.Success->{
                        _connectionState.value = ConnectionResultState(connectionStatus = it.data)
                    }
                    is ResultState.Error->{
                        _connectionState.value = ConnectionResultState(error = it.message)
                    }
                }
            }
        }
    }

     fun JoinChannel(channelId: String, token:String, uid: Int) = agoraRepository.joinChannel(channelId,token,uid)

    fun startSignaling(onRoomFound: (joiningParameters) -> Unit,filterList: List<String>) = agoraRepository.startSignaling(onRoomFound,filterList)

    fun deleteChannelRoom(userId:String) = agoraRepository.deleteChannelRoom(userId)

    fun onRightSwipe(userId:String) = agoraRepository.onRightSwipe(userId)

    fun MicMute() = agoraRepository.MicMute()

    fun MicUnMute() = agoraRepository.MicUnMute()

    fun SpeakerMute() = agoraRepository.SpeakerMute()

    fun SpeakerUnMute() = agoraRepository.SpeakerUnMute()

    fun leaveChannel() = agoraRepository.leaveChannel()

    fun leaveChannelWhenOffline() = agoraRepository.leaveChannelWhenOffline()


    fun generateFourDigitRandom(): Int {
        return (1000..9999).random()
    }



}

data class ConnectionResultState(
    val isLoading:Boolean = false,
    val connectionStatus:RtcConnectionStatus? = null,
    val error:String? = null
)