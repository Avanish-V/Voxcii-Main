package com.byteapps.voxcii.Network.Utils.domain

import com.byteapps.voxcii.Utils.ResultState
import kotlinx.coroutines.flow.Flow

interface UtilsRepository {

    fun getOnlineUsers():Flow<ResultState<Int>>


}