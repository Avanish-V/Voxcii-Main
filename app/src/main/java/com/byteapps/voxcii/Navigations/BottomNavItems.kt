package com.byteapps.karigar.presentation.Screens.bottomBarScreens.BottomBar

import com.byteapps.voxcii.R
import com.byteapps.voxcii.Utils.NestedScreens


data class navItems(

    val item: String,
    val icon: Int,
    val iconBold: Int,
    val route: String
)


val items = listOf(
    navItems(
        "Home",
        R.drawable.home_normal,
        R.drawable.home_bold,
        NestedScreens.MainScreen.Home.routes
    ),
//    navItems(
//        "Search",
//        R.drawable.search_normal,
//        R.drawable.search_bold,
//        NestedScreens.MainScreen.Search.routes
//    ),
    navItems(
        "Chat",
        R.drawable.messages_normal,
        R.drawable.messages_bold,
        NestedScreens.MainScreen.Chat.UsersChat.routes
    ),
    navItems(
        "Notification",
        R.drawable.notification_normal,
        R.drawable.notification_bold,
        NestedScreens.MainScreen.Notification.routes
    ),
    navItems(
        "Account",
        R.drawable.user_normal,
        R.drawable.user_bold,
        NestedScreens.MainScreen.Account.Profile.routes
    )
)