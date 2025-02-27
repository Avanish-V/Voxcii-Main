package com.byteapps.voxcii

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.byteapps.savvy.Navigations.Navigation
import com.byteapps.voxcii.Agora.AgoraViewModel
import com.byteapps.voxcii.Authentication.GoogleAuthentication.GoogleSignInViewModel
import com.byteapps.voxcii.Koin.appModule
import com.byteapps.voxcii.Network.UserProfile.presentation.UserProfileViewModel
import com.byteapps.voxcii.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {

    val agoraViewModel:AgoraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin{
            androidContext(this@MainActivity)
            modules(appModule)
        }

        installSplashScreen()

        setContent {

            val navHostController = rememberNavController()
            val userProfileViewModel = koinInject<UserProfileViewModel>()
            val googleAuthViewModel = koinInject<GoogleSignInViewModel>()
            val agoraViewModel = koinInject<AgoraViewModel>()

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppTheme.colorScheme.background
                ) {

                    Navigation(
                        navHostController = navHostController,
                        lifecycleScope = lifecycleScope,
                        googleAuthViewModel = googleAuthViewModel,
                        agoraViewModel = agoraViewModel,
                        userProfileViewModel = userProfileViewModel
                    )

                }
                DisposableEffect(Unit) {
                    onDispose {


                    }
                }
            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraViewModel.deleteChannelRoom(
            FirebaseAuth.getInstance().currentUser?.uid.toString()
        )
        agoraViewModel.leaveChannel()
    }
}



