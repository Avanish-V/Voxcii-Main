package com.byteapps.voxcii.Network.UserProfile.data

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Keep  // Prevent ProGuard from stripping the class
@IgnoreExtraProperties  // Allows Firebase to ignore unknown fields
@Serializable
data class UserBasicProfileDTO(
    @SerialName("_id") val _id: String = "",
    @SerialName("userName") val userName: String = "",
    @SerialName("userImage") val userImage: String = "",
    @SerialName("userEmail") val userEmail: String = "",
    @SerialName("userBio") val userBio: String = "",
    @SerialName("social") val social: String = "",
    @SerialName("likes") val likes: Int? = null,
    @SerialName("interests") val interests: List<String> = emptyList(),
    @SerialName("metaData") val metaData: MetaData = MetaData()
)



@Serializable
data class MetaData(
    @SerialName("isFirstUser")val isFirstUser:Boolean = false,
    @SerialName("createdAt")val createdAt:String?=null,
    @SerialName("updatedAt")val updatedAt:String?=null
)
