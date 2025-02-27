package com.byteapps.voxcii.Network.JoinedOptions.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.voxcii.Network.JoinedOptions.data.ConnectRequestDTO
import com.byteapps.voxcii.Network.JoinedOptions.data.RequestNotificationDTO
import com.byteapps.voxcii.Network.JoinedOptions.domain.JoinedOptionsRepository
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JoinedOptionsViewModel(private val joinedOptionsRepository: JoinedOptionsRepository):ViewModel() {

    private val _connectionRequests:MutableStateFlow<List<RequestNotificationDTO>> = MutableStateFlow(emptyList())
    val connectionRequest:StateFlow<List<RequestNotificationDTO>> = _connectionRequests.asStateFlow()

    private val _connections:MutableStateFlow<ConnectionsResultState> = MutableStateFlow(ConnectionsResultState())
    val connections:StateFlow<ConnectionsResultState> = _connections.asStateFlow()


    private val _connectionCount:MutableStateFlow<Int> = MutableStateFlow(0)
    val connectionCount:StateFlow<Int> = _connectionCount.asStateFlow()


    suspend fun sendConnectionRequest(receiverId:String,connectRequestDTO: ConnectRequestDTO) = joinedOptionsRepository.sendConnectRequest(receiverId,connectRequestDTO)

    suspend fun sendLike(receiverId:String,currentLikesCount:Int,isLike:Boolean) = joinedOptionsRepository.sendLike(receiverId,currentLikesCount,isLike)

    fun getRequests(){
        viewModelScope.launch {
            joinedOptionsRepository.getNotifications()
                .collect{
                    when(it){
                        is ResultState.Loading->{

                        }
                        is ResultState.Success->{
                            _connectionRequests.value = it.data
                        }
                        is ResultState.Error->{

                        }
                    }
                }
        }
    }

    fun getConnections(){
        viewModelScope.launch {
            joinedOptionsRepository.getConnections()
                .collect{
                    when(it){
                        is ResultState.Loading->{
                            _connections.value = ConnectionsResultState(isLoading = true)

                        }
                        is ResultState.Success->{
                            _connections.value = ConnectionsResultState(connectionList = it.data)
                        }
                        is ResultState.Error->{
                            _connections.value = ConnectionsResultState(error = it.message)
                        }
                    }
                }
        }
    }

    suspend fun acceptConnectionRequest(senderId:String) = joinedOptionsRepository.acceptConnectionRequest(senderId)

    suspend fun rejectConnectionRequest(senderId:String) = joinedOptionsRepository.rejectConnectionRequest(senderId)

    fun getConnectionCount(){
        viewModelScope.launch {
            joinedOptionsRepository.getConnectionsCount()
                .collect{
                    when(it){
                        is ResultState.Loading->{}
                        is ResultState.Success->{
                            _connectionCount.value = it.data
                        }
                        is ResultState.Error->{}


                    }
                }
        }
    }

}

data class ConnectionsResultState(
    val isLoading:Boolean = false,
    val connectionList:List<RequestNotificationDTO> = emptyList(),
    val error:String = ""
)