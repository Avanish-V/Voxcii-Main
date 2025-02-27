package com.byteapps.voxcii.Agora.media

enum class ConnectionStatus {
    CHANNEL_JOINED,
    CHANNEL_NULL,
    IS_USER_OFFLINE
}

data class RtcConnectionStatus(
    val status: ConnectionStatus,
    val channelId:String = ""
)