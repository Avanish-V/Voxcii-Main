package com.byteapps.voxcii.Network.Chats.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.voxcii.Network.Chats.data.ReceiveMessageDTO
import com.byteapps.voxcii.Network.Chats.data.UserChatsDTO
import com.byteapps.voxcii.Network.Chats.domain.ChatRepository
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatsViewModel(private val chatRepository: ChatRepository):ViewModel() {

    private val _userChats:MutableStateFlow<UserChatsResultState> = MutableStateFlow(UserChatsResultState())
    val userChats:StateFlow<UserChatsResultState> = _userChats.asStateFlow()

    private val _chats: MutableStateFlow<List<ReceiveMessageDTO>> = MutableStateFlow(emptyList())
    val chats:StateFlow<List<ReceiveMessageDTO>> = _chats.asStateFlow()

    private val _isActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isActive:StateFlow<Boolean> = _isActive.asStateFlow()

    private val _isUserTyping: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUserTyping:StateFlow<Boolean> = _isUserTyping.asStateFlow()

    private val _isSeen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSeen:StateFlow<Boolean> = _isSeen.asStateFlow()

    fun sendMessages(message: String,receiverId: String,senderId:String) = chatRepository.sendMessage(message,receiverId,senderId)

    fun updateIsUserActive(isActive:Boolean,senderId: String,receiverId: String) = chatRepository.updateIsUserActive(isActive,senderId,receiverId)

    fun updateIsUserTyping(isActive:Boolean,senderId: String,receiverId: String) = chatRepository.updateIsUserTyping(isActive,senderId,receiverId)

    fun updateIsSeen(isSeen:Boolean,senderId: String,receiverId: String) = chatRepository.updateIsSeen(isSeen,senderId,receiverId)

    fun receiveMessage(senderId: String,receiverId: String){
        viewModelScope.launch {
            chatRepository.receiveMessage(senderId,receiverId).collect{
                when(it){
                    is ResultState.Loading->{}
                    is ResultState.Success->{
                        _chats.value = it.data
                    }
                    is ResultState.Error->{}
                }

            }
        }
    }

    fun getIsActive(senderId: String,receiverId: String){
        viewModelScope.launch {
            chatRepository.getIsUserActive(senderId,receiverId).collect{
                _isActive.value = it
                Log.d("ONLINE_STATUS", "getIsActive: $it")
            }
        }
    }

    fun getUserIsTyping(senderId: String,receiverId: String){
        viewModelScope.launch {
            chatRepository.getIsUserTyping(senderId,receiverId).collect{
                _isUserTyping.value = it
            }
        }
    }

    fun getIsSeen(senderId: String,receiverId: String) {
        viewModelScope.launch {
            chatRepository.getIsSeen(senderId, receiverId).collect {
                _isSeen.value = it
            }
        }
    }

    fun getChats(){
        viewModelScope.launch {
            chatRepository.getChats().collect{
                when(it) {
                    is ResultState.Loading -> {
                        _userChats.value = UserChatsResultState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _userChats.value = UserChatsResultState(userChats = it.data)
                    }

                    is ResultState.Error -> {
                        _userChats.value = UserChatsResultState(error = it.message)
                    }
                }
            }
        }
    }


}

data class UserChatsResultState(
    val isLoading:Boolean = false,
    val userChats:List<UserChatsDTO> = emptyList(),
    val error:String = ""
)

