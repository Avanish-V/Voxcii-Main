package com.byteapps.voxcii.Agora.media

data class joiningParameters(
    val channelName:String,
    val joinedUser:String? = null,
    val uid:Int?=null,
    val token:String
)
