package com.byteapps.voxcii.Screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.byteapps.voxcii.Network.Chats.presentation.ChatsViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.Screens.SearchFlow.MentorSingleCard
import com.byteapps.voxcii.UIComponents.LoadingUI
import com.byteapps.voxcii.UIComponents.StatusScreen
import com.byteapps.voxcii.Utils.NestedScreens
import com.byteapps.voxcii.ui.theme.AppTheme
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navHostController: NavHostController,
    chatsViewModel: ChatsViewModel
) {

    val chatList = chatsViewModel.userChats.collectAsState().value

    LaunchedEffect(!chatList.isLoading) {
        if (chatList.userChats.isEmpty()){
            chatsViewModel.getChats()
        }

    }

    Scaffold (
        topBar ={
            TopAppBar(
                title = { Text("Messages", style = AppTheme.typography.headingLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background
                )
            )
        },
        containerColor = AppTheme.colorScheme.background
    ){ paddingValues ->

        StatusScreen(
            isActive = chatList.userChats.isEmpty(),
            text = "No Messages!",
            image = R.drawable.chat_icon_three_d
        )

        LoadingUI(isLoading = chatList.isLoading)

        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(paddingValues),

        ){
            items(chatList.userChats){
                MentorSingleCard(chatItem = it) {
                    navHostController.navigate(NestedScreens.MainScreen.Account.SendMessage.routes).apply {
                        navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_ID",it.receiverId)
                        navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_NAME",it.userName)
                        navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_IMAGE",it.userImage)
                    }
                }
            }
        }

    }

}