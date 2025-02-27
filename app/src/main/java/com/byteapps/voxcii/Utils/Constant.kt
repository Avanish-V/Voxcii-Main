package com.byteapps.voxcii.Utils

import com.byteapps.voxcii.R

sealed class NestedScreens(val routes:String){

    object Register : NestedScreens("Register"){

        object Login : NestedScreens("login")

    }


    object MainScreen : NestedScreens("main") {

        object Home : NestedScreens("home")

        object Search : NestedScreens("search")

        object Chat : NestedScreens("chat"){
            object UsersChat : NestedScreens("user_chats")
            object SendMessage : NestedScreens("send_Message")

        }

        object Notification : NestedScreens("notification")

        object Account : NestedScreens("account"){

            object Profile : NestedScreens("profile")
            object Connections : NestedScreens("connections")
            object ConnectionProfile : NestedScreens("connection_profile")
            object SendMessage : NestedScreens("send_message")

        }

    }
}





