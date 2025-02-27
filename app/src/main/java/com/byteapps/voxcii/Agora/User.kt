package com.byteapps.voxcii.Agora

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("uid")val uid:String = "",
    @SerialName("channel")val channel:String = ""
)
