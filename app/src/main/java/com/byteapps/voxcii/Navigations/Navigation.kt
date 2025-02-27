package com.byteapps.savvy.Navigations

import android.app.Activity.RESULT_OK
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import com.byteapps.nestednevigation.BottomAppBar
import com.byteapps.voxcii.Agora.AgoraViewModel
import com.byteapps.voxcii.Authentication.GoogleAuthentication.GoogleSignInViewModel
import com.byteapps.voxcii.Network.Chats.presentation.ChatsViewModel
import com.byteapps.voxcii.Network.UserProfile.presentation.UserProfileViewModel
import com.byteapps.voxcii.Network.Utils.presentation.UtilsViewModel
import com.byteapps.voxcii.Screens.ChatScreen
import com.byteapps.voxcii.Screens.ConnectionProfileScreen
import com.byteapps.voxcii.Screens.ConnectionsScreen
import com.byteapps.voxcii.Screens.MainScreen
import com.byteapps.voxcii.Screens.NotificationsScreen
import com.byteapps.voxcii.Screens.ProfileScreen
import com.byteapps.voxcii.Screens.Register.SignInScreen
import com.byteapps.voxcii.Screens.SearchFlow.SearchScreen
import com.byteapps.voxcii.Screens.SendMessageScreen
import com.byteapps.voxcii.Utils.NestedScreens

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Navigation(
    navHostController: NavHostController,
    lifecycleScope: LifecycleCoroutineScope,
    googleAuthViewModel:GoogleSignInViewModel,
    agoraViewModel: AgoraViewModel,
    userProfileViewModel: UserProfileViewModel
) {

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val chatsViewModel = koinInject<ChatsViewModel>()
    val utilsViewModel = koinInject<UtilsViewModel>()


    val showBottomBar by remember(currentDestination) {
        mutableStateOf(
            currentDestination in listOf(
                NestedScreens.MainScreen.Home.routes,
                NestedScreens.MainScreen.Chat.UsersChat.routes,
                NestedScreens.MainScreen.Notification.routes,
                NestedScreens.MainScreen.Account.Profile.routes
            )
        )
    }

    Column() {

        NavHost(
            modifier = Modifier.background(Color.White).weight(1f),
            navController = navHostController,
            startDestination = if (googleAuthViewModel.currentUser()) NestedScreens.MainScreen.routes else NestedScreens.Register.routes
        ) {

            navigation(
                startDestination = NestedScreens.Register.Login.routes,
                route = NestedScreens.Register.routes
            ){
                composable(route = NestedScreens.Register.Login.routes) {

                    val state = googleAuthViewModel.state.collectAsStateWithLifecycle()

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = {result->
                            if (result.resultCode == RESULT_OK) {
                                lifecycleScope.launch {
                                    googleAuthViewModel.onSignInResult(
                                        intent = result.data?:return@launch
                                    )

                                }
                            }

                        }
                    )

                    LaunchedEffect(key1 = state.value.isSignInSuccessful) {
                        if (state.value.isSignInSuccessful){
                            Log.d("USER_AUTH_ID",FirebaseAuth.getInstance().currentUser!!.uid)
                            googleAuthViewModel.verifyUser(FirebaseAuth.getInstance().currentUser!!.uid)
                                .collect{
                                    Log.d("USER_AUTH_ID",it.toString())
                                    if (it.status){
                                        navHostController.navigate(NestedScreens.MainScreen.Home.routes){
                                            popUpTo(NestedScreens.Register.routes){
                                                inclusive = true
                                            }
                                        }
                                    }
                                }

                        }
                    }

                    SignInScreen(
                        navHostController = navHostController,
                        onSignInClick = {

                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthViewModel.startSignIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender?:return@launch
                                    ).build()
                                )
                            }

                        }
                    )

                }


            }

            navigation(
                startDestination = NestedScreens.MainScreen.Home.routes,
                route = NestedScreens.MainScreen.routes
            ) {
                composable(route = NestedScreens.MainScreen.Home.routes) {
                    MainScreen(
                        navHostController = navHostController,
                        userProfileViewModel = userProfileViewModel,
                        utilsViewModel = utilsViewModel,
                        agoraViewModel = agoraViewModel
                    )
                }
                composable(route = NestedScreens.MainScreen.Search.routes) {
                    SearchScreen(navHostController = navHostController)
                }

                navigation(
                    startDestination = NestedScreens.MainScreen.Chat.UsersChat.routes,
                    route = NestedScreens.MainScreen.Chat.routes
                ){
                    composable(route = NestedScreens.MainScreen.Chat.UsersChat.routes) {
                        ChatScreen(
                            navHostController = navHostController,
                            chatsViewModel = chatsViewModel
                        )
                    }
                    composable(route = NestedScreens.MainScreen.Chat.SendMessage.routes) {
                        SendMessageScreen(navHostController = navHostController)
                    }

                }

                composable(route = NestedScreens.MainScreen.Notification.routes) {
                    NotificationsScreen()
                }

                navigation(
                    startDestination = NestedScreens.MainScreen.Account.Profile.routes,
                    route = NestedScreens.MainScreen.Account.routes
                ){
                    composable(route = NestedScreens.MainScreen.Account.Profile.routes) {
                        ProfileScreen(
                            navHostController = navHostController,
                            userProfileViewModel = userProfileViewModel
                        )
                    }

                    composable(route = NestedScreens.MainScreen.Account.Connections.routes) {
                        ConnectionsScreen(
                            navHostController = navHostController
                        )
                    }
                    composable(route = NestedScreens.MainScreen.Account.ConnectionProfile.routes) {
                        ConnectionProfileScreen(
                            navHostController = navHostController
                        )
                    }
                    composable(route = NestedScreens.MainScreen.Account.SendMessage.routes) {
                        SendMessageScreen(
                            navHostController = navHostController
                        )
                    }
                }


            }
        }

        AnimatedVisibility(
            visible = showBottomBar,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
        ) {
            BottomAppBar(navController = navHostController)
        }

    }

}
