package com.byteapps.voxcii.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byteapps.voxcii.Network.Chats.presentation.ChatsViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.Utils.ResultState
import com.byteapps.voxcii.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMessageScreen(navHostController: NavHostController) {

    val chatsViewModel = koinInject<ChatsViewModel>()
    val scope = rememberCoroutineScope()

    val userUUID by remember {
        mutableStateOf(navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("USER_ID"))
    }
    val userName by remember {
        mutableStateOf(navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("USER_NAME"))
    }
    val userImage by remember {
        mutableStateOf(navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("USER_IMAGE"))
    }

    val currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid.toString()) }

    val chats = chatsViewModel.chats.collectAsStateWithLifecycle().value
    val isActive = chatsViewModel.isActive.collectAsStateWithLifecycle().value
    val isUserTyping = chatsViewModel.isUserTyping.collectAsStateWithLifecycle().value
    val isSeen = chatsViewModel.isSeen.collectAsStateWithLifecycle().value


    var messageText by remember { mutableStateOf("") }

    var isTyping by remember { mutableStateOf(false) }  // Track typing state
    val senderId = FirebaseAuth.getInstance().currentUser!!.uid

    LaunchedEffect(messageText) {
        if (messageText.isNotEmpty()) {
            if (!isTyping) {
                isTyping = true
                chatsViewModel.updateIsUserTyping(true, senderId, userUUID.toString())
            }
            delay(2000L)  // Wait for 2 seconds of inactivity
            if (messageText == messageText) { // Still same text after delay?
                isTyping = false
                chatsViewModel.updateIsUserTyping(false, senderId, userUUID.toString())
            }
        } else {
            if (isTyping) {
                isTyping = false
                chatsViewModel.updateIsUserTyping(false, senderId, userUUID.toString())
            }
        }
    }

    LaunchedEffect(Unit) {
        chatsViewModel.updateIsSeen(
            isSeen = true,
            senderId = FirebaseAuth.getInstance().currentUser!!.uid,
            receiverId = userUUID.toString()
        )
    }

    LaunchedEffect (Unit) {
        chatsViewModel.getIsSeen(
            senderId = FirebaseAuth.getInstance().currentUser!!.uid,
            receiverId = userUUID.toString()
        )
    }

    LaunchedEffect (Unit){
        chatsViewModel.receiveMessage(
            senderId = FirebaseAuth.getInstance().currentUser!!.uid,
            receiverId = userUUID.toString()
        )
    }


    LaunchedEffect(Unit) {
        chatsViewModel.updateIsUserActive(
            isActive = true,
            senderId = FirebaseAuth.getInstance().currentUser!!.uid,
            receiverId = userUUID.toString()
        )
    }

    LaunchedEffect (Unit){
        chatsViewModel.getIsActive(
            senderId = FirebaseAuth.getInstance().currentUser!!.uid,
            receiverId = userUUID.toString()
        )
    }

    LaunchedEffect (Unit) {
        chatsViewModel.getUserIsTyping(
            senderId = FirebaseAuth.getInstance().currentUser!!.uid,
            receiverId = userUUID.toString()
        )
    }

    DisposableEffect (Unit){
        onDispose {
            chatsViewModel.updateIsUserActive(
                isActive = false,
                senderId = FirebaseAuth.getInstance().currentUser!!.uid,
                receiverId = userUUID.toString()
            )
        }
    }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.typing))


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(elevation = 0.5.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        BadgedBox(
                            badge = {
                                if (isActive){
                                    Box(
                                        modifier = Modifier
                                            .offset(x = 4.dp, y = 22.dp)
                                            .size(12.dp)
                                            .background(color = AppTheme.colorScheme.primary, shape = CircleShape)
                                            .border(width = 1.dp, color = Color.White, shape = CircleShape)
                                    )
                                }
                            }
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape),
                                model =userImage ,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )

                        }

                        userName?.let { Text(it, style = AppTheme.typography.titleLarge) }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = AppTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background,
                    titleContentColor = AppTheme.colorScheme.onTertiary
                ),

            )
        },
        containerColor = AppTheme.colorScheme.background
    ) {padding->


        Column(modifier = Modifier.padding(padding)) {

            Box(modifier = Modifier.weight(1f)){

                LazyColumn (
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    items(chats){

                        if (it.user == currentUser){

                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ){
                                Box (
                                    modifier = Modifier.background(
                                        color = AppTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 0.dp,
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp
                                        )
                                    )
                                ){
                                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = it.message,
                                            style = AppTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Row (
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ){
                                            Text(
                                                text = convertTimestampToTime(it.timestamp.toLong()),
                                                fontSize = 12.sp
                                            )
                                        }

                                    }
                                }
                            }
                        }else{
                            Column (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(5.dp)){
                                Box (
                                    modifier = Modifier.background(
                                        color = AppTheme.colorScheme.onPrimary,
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp,
                                            bottomStart = 0.dp,
                                            bottomEnd = 12.dp
                                        )
                                    )
                                ){
                                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = it.message,
                                            style = AppTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Text(
                                            text = convertTimestampToTime(it.timestamp.toLong()),
                                            fontSize = 12.sp
                                        )
                                    }


                                }


                            }
                        }


                    }
                    item {
                        if (isUserTyping){
                           Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart){
                               LottieAnimation(
                                   modifier = Modifier.size(60.dp),
                                   composition = composition,
                                   iterations = LottieConstants.IterateForever,
                                   speed = 1f,
                                   restartOnPlay = true,
                               )
                           }
                        }
                    }
                }


            }

            Box(){
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = messageText,
                    onValueChange = {
                        messageText = it
                    },
                    placeholder = {
                        Text("Write a text...")
                    },
                    trailingIcon = {
                        IconButton(onClick = {

                            scope.launch {
                                chatsViewModel.sendMessages(
                                    message = messageText,
                                    receiverId = userUUID.toString(),
                                    senderId = FirebaseAuth.getInstance().currentUser!!.uid
                                ).collect{
                                    when(it){
                                        is ResultState.Loading->{
                                            Log.d("FIREBASE_STATUS",it.toString())
                                        }
                                        is ResultState.Success->{
                                           messageText = ""
                                        }
                                        is ResultState.Error->{
                                            Log.d("FIREBASE_STATUS",it.toString())
                                        }
                                    }
                                }
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null
                            )
                        }
                    },
                    textStyle = AppTheme.typography.titleMedium,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = AppTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = AppTheme.colorScheme.onPrimary,
                        disabledContainerColor = AppTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }

}

fun convertTimestampToTime(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Example: 02:30 PM
         sdf.format(Date(timestamp)).toString()
    }catch (e:Exception){
        e.printStackTrace().toString()
    }

}