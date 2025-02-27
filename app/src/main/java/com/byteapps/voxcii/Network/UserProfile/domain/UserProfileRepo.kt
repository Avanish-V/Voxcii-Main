package com.byteapps.voxcii.Network.UserProfile.domain

import com.byteapps.voxcii.Network.UserProfile.data.UserBasicProfileDTO
import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface UserProfileRepo {

    suspend fun getBaseProfile(userId:String):Flow<ResultState<UserBasicProfileDTO>>

    suspend fun getUserProfileById(channelId:String):Flow<ResultState<UserBasicProfileDTO>>

    fun updateUserName(userName:String):Flow<ResultState<Boolean>>

    fun updateSocialAccounts(accounts:String):Flow<ResultState<Boolean>>

    fun updateAbout(about:String):Flow<ResultState<Boolean>>

    fun updateInterests(interests:List<String>):Flow<ResultState<Boolean>>


}