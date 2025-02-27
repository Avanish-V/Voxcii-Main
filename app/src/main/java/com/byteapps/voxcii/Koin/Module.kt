package com.byteapps.voxcii.Koin

import com.byteapps.voxcii.Agora.AgoraRepoImpl
import com.byteapps.voxcii.Agora.AgoraRepository
import com.byteapps.voxcii.Agora.AgoraViewModel
import com.byteapps.voxcii.Authentication.GoogleAuthentication.GoogleAuthRepo
import com.byteapps.voxcii.Authentication.GoogleAuthentication.GoogleAuthUiClient
import com.byteapps.voxcii.Authentication.GoogleAuthentication.GoogleSignInViewModel
import com.byteapps.voxcii.Network.Chats.data.ChatImpl
import com.byteapps.voxcii.Network.Chats.domain.ChatRepository
import com.byteapps.voxcii.Network.Chats.presentation.ChatsViewModel
import com.byteapps.voxcii.Network.JoinedOptions.data.JoinedOptionsImpl
import com.byteapps.voxcii.Network.JoinedOptions.domain.JoinedOptionsRepository
import com.byteapps.voxcii.Network.JoinedOptions.presentation.JoinedOptionsViewModel
import com.byteapps.voxcii.Network.UserProfile.data.UserProfileImpl
import com.byteapps.voxcii.Network.UserProfile.domain.UserProfileRepo
import com.byteapps.voxcii.Network.UserProfile.presentation.UserProfileViewModel
import com.byteapps.voxcii.Network.Utils.data.UtilsImpl
import com.byteapps.voxcii.Network.Utils.domain.UtilsRepository
import com.byteapps.voxcii.Network.Utils.presentation.UtilsViewModel
import com.byteapps.wiseschool.GeoFencing.Permission.PermissionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

val appModule = module {

    // Provide Firebase Auth instance
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single <FirebaseDatabase>{ FirebaseDatabase.getInstance() }
    single <AgoraRepository>{ AgoraRepoImpl(get()) }

    single<UserProfileRepo> { UserProfileImpl(get(),get(),get()) }
    single<JoinedOptionsRepository> { JoinedOptionsImpl(get(),get()) }
    single <ChatRepository>{ ChatImpl(get(),get(),get()) }
    single<GoogleAuthRepo> { GoogleAuthUiClient(androidContext(),get(),get()) }
    single<UtilsRepository> { UtilsImpl(get()) }


    viewModel { GoogleSignInViewModel(get()) }
    viewModel { UserProfileViewModel(get()) }
    viewModel { AgoraViewModel(get()) }
    viewModel { PermissionViewModel() }
    viewModel { JoinedOptionsViewModel(get()) }
    viewModel { ChatsViewModel(get()) }
    viewModel { UtilsViewModel(get()) }

}
