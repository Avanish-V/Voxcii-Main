package com.byteapps.voxcii.Network.Utils.presentation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.voxcii.Network.Utils.domain.UtilsRepository
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UtilsViewModel(private val utilsRepository: UtilsRepository):ViewModel() {

    private val _onlineUsers : MutableStateFlow<OnlineUsersResultState> = MutableStateFlow(OnlineUsersResultState())
    val onlineUsers:StateFlow<OnlineUsersResultState> = _onlineUsers.asStateFlow()

    init {
        viewModelScope.launch {
            utilsRepository.getOnlineUsers().collect{

                when(it){
                    is ResultState.Loading -> {
                        _onlineUsers.value = OnlineUsersResultState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _onlineUsers.value = OnlineUsersResultState(onlineUsers = it.data)
                    }
                    is ResultState.Error->{
                        _onlineUsers.value = OnlineUsersResultState(error = it.message)
                    }
                }
            }
        }
    }

}

data class OnlineUsersResultState(
    val isLoading:Boolean = false,
    val onlineUsers:Int = 0,
    val error:String = ""
)