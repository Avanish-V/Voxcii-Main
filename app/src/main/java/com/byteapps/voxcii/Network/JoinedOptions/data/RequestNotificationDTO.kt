package com.byteapps.voxcii.Network.JoinedOptions.data

data class RequestNotificationDTO(
    val userName:String = "",
    val userImage:String = "",
    val userBio:String = "",
    val senderId:String = "",
    val status:Boolean = false

)
