package com.byteapps.voxcii.Network.UserProfile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byteapps.voxcii.Network.UserProfile.data.UserBasicProfileDTO
import com.byteapps.voxcii.Network.UserProfile.domain.UserProfileRepo
import com.byteapps.voxcii.Utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(private val userProfileRepo: UserProfileRepo):ViewModel() {


    private val _userBaseProfile: MutableStateFlow<UserBaseProfileResultState> = MutableStateFlow(UserBaseProfileResultState())
    val userBaseProfile: StateFlow<UserBaseProfileResultState> = _userBaseProfile.asStateFlow()

    private val _joinedUserProfile: MutableStateFlow<JoinedUserResultState> = MutableStateFlow(JoinedUserResultState())
    val joinedUserProfile: StateFlow<JoinedUserResultState> = _joinedUserProfile.asStateFlow()

    fun getUserProfile(userId:String){
        viewModelScope.launch {
            userProfileRepo.getBaseProfile(userId).collect{
                when(it){
                    is ResultState.Loading->{
                        _userBaseProfile.value = UserBaseProfileResultState(isLoading = true)
                    }
                    is ResultState.Success->{
                        _userBaseProfile.value = UserBaseProfileResultState(baseProfileData = it.data)
                    }
                    is ResultState.Error->{
                        _userBaseProfile.value = UserBaseProfileResultState(error = it.message)
                    }
                }
            }
        }
    }

    fun getJoinedUserProfile(channelId:String){
        viewModelScope.launch {
            userProfileRepo.getUserProfileById(channelId).collect{
                when(it){
                    is ResultState.Loading->{
                        _joinedUserProfile.value = JoinedUserResultState(isLoading = true)
                    }
                    is ResultState.Success->{

                        _joinedUserProfile.value = JoinedUserResultState(joinedUser = it.data)
                    }
                    is ResultState.Error->{
                        _joinedUserProfile.value = JoinedUserResultState(error = it.message)
                    }
                }
            }
        }
    }

    init {
        FirebaseAuth.getInstance().currentUser?.let { getUserProfile(userId = it.uid) }
    }

    fun modifyName(userName:String) = userProfileRepo.updateUserName(userName)

    fun modifyAbout(about:String) = userProfileRepo.updateAbout(about)

    fun modifySocialAccount(social:String) = userProfileRepo.updateSocialAccounts(social)

    fun modifyInterests(interests:List<String>) = userProfileRepo.updateInterests(interests)



}


data class UserBaseProfileResultState(
    val isLoading:Boolean = false,
    val baseProfileData: UserBasicProfileDTO? = null,
    val error:String = ""
)

data class JoinedUserResultState(
    val isLoading:Boolean = false,
    val joinedUser: UserBasicProfileDTO? = null,
    val error:String = ""
)
