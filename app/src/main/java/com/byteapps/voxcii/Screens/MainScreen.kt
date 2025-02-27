package com.byteapps.voxcii.Screens

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.byteapps.savvy.LocationPermission.Permission.StateDialog
import com.byteapps.voxcii.Agora.AgoraViewModel
import com.byteapps.voxcii.Agora.ConnectionResultState
import com.byteapps.voxcii.Agora.media.ConnectionStatus
import com.byteapps.voxcii.Agora.media.RtcConnectionStatus
import com.byteapps.voxcii.Network.JoinedOptions.data.ConnectRequestDTO
import com.byteapps.voxcii.Network.JoinedOptions.presentation.JoinedOptionsViewModel
import com.byteapps.voxcii.Network.UserProfile.data.UserBasicProfileDTO
import com.byteapps.voxcii.Network.UserProfile.presentation.UserProfileViewModel
import com.byteapps.voxcii.Network.Utils.presentation.UtilsViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.UIComponents.MatchingLoadingAnimated
import com.byteapps.voxcii.Utils.CallTimerScreen
import com.byteapps.voxcii.Utils.CallTimerViewModel
import com.byteapps.voxcii.Utils.CallingTimerScreen
import com.byteapps.voxcii.Utils.ResultState
import com.byteapps.voxcii.ui.theme.AppTheme
import com.byteapps.wiseschool.GeoFencing.Permission.PermissionViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navHostController: NavHostController,
    userProfileViewModel: UserProfileViewModel,
    utilsViewModel: UtilsViewModel,
    agoraViewModel: AgoraViewModel
) {


    val joinedOptionsViewModel = koinInject<JoinedOptionsViewModel>()
    val permissionViewModel = koinInject<PermissionViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val connectionState by agoraViewModel.connectionState.collectAsState()
    val userProfile by userProfileViewModel.userBaseProfile.collectAsStateWithLifecycle()
    val joinedUser by userProfileViewModel.joinedUserProfile.collectAsStateWithLifecycle()
    val onlineUsers = utilsViewModel.onlineUsers.collectAsStateWithLifecycle().value

    var maleIsChecked by remember { mutableStateOf(false) }
    var femaleIsChecked by remember { mutableStateOf(false) }
    var englishIsChecked by remember { mutableStateOf(false) }
    val referenceList by remember { mutableStateOf(mutableListOf<String>()) }

    var expanded by remember { mutableStateOf(false) }

    val recordAudioPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                agoraViewModel.AgoraInit()
                (if (referenceList.isEmpty()) userProfile.baseProfileData?.interests else referenceList)?.let {
                    agoraViewModel.startSignaling(
                        onRoomFound = {
                            coroutineScope.launch {
                                agoraViewModel.JoinChannel(
                                    it.channelName,
                                    it.token,
                                    it.uid?.toInt() ?: 0
                                )
                            }
                        },
                        filterList = it
                    )
                }
            }
        }
    )


    StateDialog(
        dialogQueue = permissionViewModel.visiblePermissionDialogQueue,
        permissionViewModel = permissionViewModel,
        context = context
    )



    var isVisible = remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Voxcii", style = AppTheme.typography.headingLarge) },
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .size(32.dp),
                        painter = painterResource(R.drawable.main_screen_logo),
                        contentDescription = null
                    )
                },
                actions = {
                    Row (){
                        IconButton(onClick = {expanded = !expanded }) {
                            Icon(
                                painter = painterResource(R.drawable.setting_filter),
                                contentDescription = null
                            )
                        }
                    }

                    DropdownMenu(
                        modifier = Modifier,
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = AppTheme.colorScheme.onPrimary
                    ) {

                        Column {

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                                Text(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    text="Preferences",
                                    style = AppTheme.typography.titleMedium
                                )
                                HorizontalDivider()
                            }

                            Column (modifier = Modifier.padding(12.dp)){

                                Column {
                                    Text(text = "Gender", style = AppTheme.typography.titleMedium)
                                    Row (verticalAlignment = Alignment.CenterVertically){

                                        Checkbox(
                                            checked = maleIsChecked,
                                            onCheckedChange = {
                                                maleIsChecked = !maleIsChecked

                                                if (maleIsChecked){
                                                    referenceList.add("Male")
                                                }else{
                                                    referenceList.remove("Male")
                                                }

                                            }
                                        )
                                        Text(text = "Male")

                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {

                                        Checkbox(
                                            checked = femaleIsChecked,
                                            onCheckedChange = {
                                                femaleIsChecked = !femaleIsChecked

                                                if (femaleIsChecked){
                                                    referenceList.add("Female")
                                                }else{
                                                    referenceList.remove("Female")
                                                }

                                            }
                                        )
                                        Text(text = "Female")

                                    }
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                                Column {
                                    Text(text = "Language", style = AppTheme.typography.titleMedium)
                                    Row (verticalAlignment = Alignment.CenterVertically){

                                        Checkbox(
                                            checked = englishIsChecked,
                                            onCheckedChange = {
                                                englishIsChecked = !englishIsChecked

                                                if (englishIsChecked){
                                                    referenceList.add("English")
                                                }else{
                                                    referenceList.remove("English")
                                                }
                                            }
                                        )
                                        Text(text = "English")

                                    }
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background
                )
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (connectionState?.isLoading == true) {
                MatchingLoadingAnimated(
                    isLoading = true,
                    userImage = userProfile.baseProfileData?.userImage.orEmpty()
                )
            }

            when (connectionState?.connectionStatus?.status) {

                ConnectionStatus.CHANNEL_JOINED -> {



                    LaunchedEffect (Unit){

                        userProfileViewModel.getJoinedUserProfile(
                            channelId = connectionState?.connectionStatus?.channelId.orEmpty()
                        )
                    }
                    isVisible.value = false


                        VerticalSwipeButton(
                            onSwipeComplete = {
                                coroutineScope.launch {
                                    // Step 1: Leave Agora Channel
                                    agoraViewModel.leaveChannel()

                                    // Step 2: Delete the channel room
                                    agoraViewModel.deleteChannelRoom(
                                        FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
                                    )

                                    // Step 3: Reinitialize Agora
                                    agoraViewModel.AgoraInit()

                                    // Step 4: Start signaling and join the channel
                                    agoraViewModel.startSignaling(
                                        onRoomFound = {
                                            agoraViewModel.JoinChannel(
                                                it.channelName,
                                                it.token,
                                                it.uid?.toInt() ?: 0
                                            )
                                        },
                                        filterList = referenceList
                                    )
                                }
                            },
                            coroutineScope = coroutineScope,
                            joinedOptionsViewModel = joinedOptionsViewModel,
                            userBasicProfileDTO = joinedUser.joinedUser,
                            agoraViewModel = agoraViewModel,
                            isUserOffline = false,
                            isCallCancel = {

                            }
                        )




                }

                ConnectionStatus.IS_USER_OFFLINE -> {

                    isVisible.value = false

                    LaunchedEffect(Unit) {  // Run leaveChannel in a coroutine
                        withContext(Dispatchers.IO) {
                            agoraViewModel.leaveChannel()
                        }
                    }



                        VerticalSwipeButton(
                            onSwipeComplete = {

                                coroutineScope.launch(Dispatchers.IO) { // Run AgoraInit in background
                                    agoraViewModel.AgoraInit()
                                    agoraViewModel.startSignaling(
                                        onRoomFound = {
                                            agoraViewModel.JoinChannel(
                                                it.channelName,
                                                it.token,
                                                it.uid?.toInt() ?: 0
                                            )
                                        },
                                        filterList = referenceList
                                    )
                                }

                            },
                            coroutineScope = coroutineScope,
                            joinedOptionsViewModel = joinedOptionsViewModel,
                            userBasicProfileDTO = joinedUser.joinedUser,
                            agoraViewModel = agoraViewModel,
                            isUserOffline = true,
                            isCallCancel = {

                            }
                        )




                }
                else -> {



                }
            }


            if (isVisible.value){

                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "Connect Through Voice", style = AppTheme.typography.headingLarge)

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Meet interesting people from around the world", style = AppTheme.typography.titleSmall)

                        Spacer(modifier = Modifier.height(40.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ){
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(R.drawable.users),
                                    contentDescription = null
                                )
                                Text("${onlineUsers.onlineUsers} Online")
                            }

                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ){
                                Icon(
                                    modifier = Modifier.size(22.dp),
                                    painter = painterResource(R.drawable.globe),
                                    contentDescription = null,
                                    tint = AppTheme.colorScheme.onTertiary
                                )
                                Text("120 Countries")
                            }
                        }

                    }


                    Column(modifier = Modifier.weight(1f)) {

                        val gradientBrush = Brush.horizontalGradient(
                            colors = listOf(
                                AppTheme.colorScheme.primary,
                                AppTheme.colorScheme.tertiary,
                                AppTheme.colorScheme.secondary,

                                ) // Customize your gradient colors
                        )

                        Card(
                            modifier = Modifier
                                .height(50.dp)
                                .width(180.dp)
                                .shadow(
                                    elevation = 20.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = AppTheme.colorScheme.primary,
                                    spotColor = AppTheme.colorScheme.tertiary
                                ),
                            onClick = {recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)}
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = gradientBrush
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.microphone_normal),
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Text(
                                        "Start Matching",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

    }
}

@Composable
fun VerticalSwipeButton(
    onSwipeComplete: () -> Unit,
    coroutineScope: CoroutineScope,
    joinedOptionsViewModel: JoinedOptionsViewModel,
    userBasicProfileDTO: UserBasicProfileDTO?,
    agoraViewModel: AgoraViewModel,
    isUserOffline: Boolean,
    isCallCancel: (Boolean) -> Unit
) {
    
    val swipeableState = remember { mutableStateOf(0f) }
    var buttonEnabled by remember { mutableStateOf(true) }
    val callTimerViewModel:CallTimerViewModel = viewModel()
    val context = LocalContext.current
    val maxSwipeDistance = 1000f  // Max swipe height before triggering
    val threshold = 500f          // Swipe threshold to trigger action

    Box(
        modifier = Modifier
            .offset { IntOffset(0, -swipeableState.value.toInt()) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    swipeableState.value =
                        (swipeableState.value - delta).coerceIn(0f, maxSwipeDistance)
                },
                onDragStopped = {
                    coroutineScope.launch {
                        if (swipeableState.value >= threshold) {
                            onSwipeComplete()
                            callTimerViewModel.resetTimer()
                            animateSwipeReset(swipeableState) // Smooth reset
                        } else {
                            animateSwipeReset(swipeableState) // Smooth reset if not swiped enough
                        }
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isUserOffline) 0.5f else 1f)
                .padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colorScheme.onPrimary)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {


                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            if (isUserOffline) {
                                Text(text = "Leaved", style = AppTheme.typography.titleMedium)
                            }
                        }

                        LikeToggleButton(
                            onToggle = {

                                    coroutineScope.launch {
                                        if (userBasicProfileDTO != null) {
                                            userBasicProfileDTO.likes?.let {
                                                joinedOptionsViewModel.sendLike(
                                                    receiverId = userBasicProfileDTO._id,
                                                    currentLikesCount = it,
                                                    isLike = true
                                                ).collect{

                                                }
                                            }
                                        }

                                    }

                            },
                            offToggle = {
                                coroutineScope.launch {
                                    if (userBasicProfileDTO != null) {
                                        userBasicProfileDTO.likes?.let {
                                            joinedOptionsViewModel.sendLike(
                                                receiverId = userBasicProfileDTO._id,
                                                currentLikesCount = it,
                                                isLike = false
                                            ).collect{

                                            }
                                        }
                                    }

                                }
                            },
                            onToggleIcon = R.drawable.heart_bold,
                            offToggleIcon = R.drawable.heart_outline,
                            description = "Like"
                        )
                    }
                }


                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            NameImage(
                                userImage = userBasicProfileDTO?.userImage ?: "",
                                userName = userBasicProfileDTO?.userName ?: ""
                            )

                            Text(
                                modifier = Modifier.padding(horizontal = 40.dp),
                                text = userBasicProfileDTO?.userBio ?: "",
                                textAlign = TextAlign.Center,
                                style = AppTheme.typography.titleMedium,
                                fontWeight = FontWeight.Normal,
                                color = AppTheme.colorScheme.onTertiary
                            )
                            CallTimerScreen(
                                viewModel = callTimerViewModel,
                                isRunning = !isUserOffline
                            )
                        }
                    }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 24.dp),

                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ToggleButton(
                        onToggle = { agoraViewModel.MicUnMute() },
                        offToggle = { agoraViewModel.MicMute() },
                        onToggleIcon = R.drawable.microphone_normal,
                        offToggleIcon = R.drawable.microphone_cros,
                        description = "Mic"
                    )

                    ToggleButton(
                        onToggle = { agoraViewModel.SpeakerUnMute() },
                        offToggle = { agoraViewModel.SpeakerMute() },
                        onToggleIcon = R.drawable.volume_normal,
                        offToggleIcon = R.drawable.volume_cross,
                        description = "Volume"
                    )

                    Card(
                        onClick = {
                            coroutineScope.launch {
                                userBasicProfileDTO?._id?.let { receiverId ->
                                    joinedOptionsViewModel.sendConnectionRequest(
                                        receiverId = receiverId,
                                        connectRequestDTO = ConnectRequestDTO(
                                            senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                                            status = false
                                        )
                                    ).collect {
                                        when (it) {
                                            is ResultState.Loading -> buttonEnabled = false
                                            is ResultState.Success -> {
                                                buttonEnabled = false
                                                Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show()
                                            }
                                            is ResultState.Error -> buttonEnabled = true
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.size(62.dp),
                        shape = RoundedCornerShape(34.dp),
                        enabled = buttonEnabled,
                        colors = CardDefaults.cardColors(containerColor = AppTheme.colorScheme.primary)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(R.drawable.profile_add),
                                contentDescription = "Add Person",
                                tint = Color.White
                            )
                        }
                    }

                    Card(
                        onClick = {

                            isCallCancel(true)

                            coroutineScope.launch {
                                agoraViewModel.leaveChannel()
                                agoraViewModel.deleteChannelRoom(
                                    FirebaseAuth.getInstance().currentUser?.uid.toString()
                                )
                            }

                            callTimerViewModel.stopTimer()

                        },
                        modifier = Modifier.size(62.dp),
                        shape = RoundedCornerShape(34.dp),
                        enabled = buttonEnabled,
                        colors = CardDefaults.cardColors(containerColor = Color.Red)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }


    }
}





suspend fun animateSwipeReset(swipeableState: MutableState<Float>) {
    animate(
        initialValue = swipeableState.value,
        targetValue = 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    ) { value, _ ->
        swipeableState.value = value
    }
}

@Composable
fun NameImage(userImage:String,userName:String) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Card(
            Modifier.size(100.dp),
            shape = CircleShape,
            border = BorderStroke(
                width = 2.dp,
                color = AppTheme.colorScheme.primary
            ),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.background
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = userImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            text = userName, style = AppTheme.typography.titleMedium,
            color = AppTheme.colorScheme.onTertiary
        )
    }


}

@Composable
fun ToggleButton(
    onToggle: () -> Unit,
    offToggle: () -> Unit,
    onToggleIcon: Int,
    offToggleIcon: Int,
    description: String
) {

    var isActive = remember { mutableStateOf(true) }

    Card(
        onClick = {
            isActive.value = !isActive.value
            if (isActive.value) {
                onToggle()
            } else {
                offToggle()
            }
        },
        modifier = Modifier.size(62.dp),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colorScheme.primary)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(if (isActive.value) onToggleIcon else offToggleIcon),
                contentDescription = description,
                tint = Color.White
            )
        }
    }


}


@Composable
fun LikeToggleButton(
    onToggle: () -> Unit,
    offToggle: () -> Unit,
    onToggleIcon: Int,
    offToggleIcon: Int,
    description: String
) {

    var isActive = remember { mutableStateOf(false) }


    IconButton(onClick = {
        isActive.value = !isActive.value
        if (isActive.value) {
            onToggle()
        } else {
            offToggle()
        }
    }) {

        Icon(
            modifier = Modifier.size(28.dp),
            painter = painterResource(if (isActive.value) onToggleIcon else offToggleIcon),
            contentDescription = "Add Person",
            tint = if (isActive.value) Color.Red else AppTheme.colorScheme.onTertiary
        )

    }

}

