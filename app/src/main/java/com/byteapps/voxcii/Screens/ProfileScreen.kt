package com.byteapps.voxcii.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.byteapps.karigar.presentation.Screens.bottomBarScreens.BottomBar.navItems
import com.byteapps.voxcii.Network.JoinedOptions.presentation.JoinedOptionsViewModel
import com.byteapps.voxcii.Network.UserProfile.presentation.UserBaseProfileResultState
import com.byteapps.voxcii.Network.UserProfile.presentation.UserProfileViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.UIComponents.CustomTextField
import com.byteapps.voxcii.UIComponents.EditScreenChange
import com.byteapps.voxcii.UIComponents.LoadingUI
import com.byteapps.voxcii.Utils.InterestSelectionViewModel
import com.byteapps.voxcii.Utils.NestedScreens
import com.byteapps.voxcii.Utils.ResultState
import com.byteapps.voxcii.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    userProfileViewModel: UserProfileViewModel
) {

    val interestSelectionViewModel:InterestSelectionViewModel = viewModel()

    val joinedOptionsViewModel = koinInject<JoinedOptionsViewModel>()

    LaunchedEffect(Unit) {
        joinedOptionsViewModel.getConnectionCount()
    }
    val userUUID by remember {
        mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid)
    }

    val scope = rememberCoroutineScope()

    val userProfile = userProfileViewModel.userBaseProfile.collectAsStateWithLifecycle().value
    val connectionCount = joinedOptionsViewModel.connectionCount.collectAsStateWithLifecycle().value

    val interestList = remember { mutableStateListOf("Entrepreneurship", "Coding","Reading") }


    var isLoading by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf("") }
    var userAbout by remember { mutableStateOf("") }
    var socialAccount by remember { mutableStateOf("") }

    var screenValue by rememberSaveable  { mutableStateOf(EditScreenChange.PROFILE_SCREEN) }

    when(screenValue){

        EditScreenChange.PROFILE_SCREEN ->{
            
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Profile", style = AppTheme.typography.headingLarge) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = AppTheme.colorScheme.background,
                            titleContentColor = AppTheme.colorScheme.onTertiary
                        ),
                        actions ={
                            Row(horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = {screenValue = EditScreenChange.SETTING_SCREEN}) {
                                    Icon(
                                        painter = painterResource(R.drawable.setting),
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                    )
                },
                containerColor = AppTheme.colorScheme.background
            ) {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    color = AppTheme.colorScheme.background
                ) {


                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        item   {


                            Row {

                                Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {

                                    val gradientBrush = Brush.horizontalGradient(
                                        colors = listOf(
                                            AppTheme.colorScheme.primary,
                                            AppTheme.colorScheme.tertiary,
                                            AppTheme.colorScheme.secondary,
                                        )
                                        // Customize your gradient colors
                                    )

                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ){

                                        Card(
                                            Modifier.size(100.dp)
                                                .border(
                                                    width = 2.dp,
                                                    color = AppTheme.colorScheme.primary,
                                                    shape = CircleShape
                                                ),
                                            shape = CircleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.Transparent
                                            )

                                        ) {

                                            Box(modifier = Modifier
                                                .fillMaxSize()
                                                .padding(6.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    brush = gradientBrush,
                                                )
                                            ) {
                                                AsyncImage(
                                                    modifier = Modifier.fillMaxSize(),
                                                    model = userProfile.baseProfileData?.userImage,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }

                                        Row (
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Column (horizontalAlignment = Alignment.Start){
                                                Text("${userProfile.baseProfileData?.likes}")
                                                Text("Likes")
                                            }

                                            VerticalDivider(
                                                color = AppTheme.colorScheme.background,
                                                modifier = Modifier.height(40.dp).padding(horizontal = 24.dp),
                                                thickness = 2.dp
                                            )

                                            Column  (
                                                modifier = Modifier.clickable {
                                                    navHostController.navigate(NestedScreens.MainScreen.Account.Connections.routes)
                                                },
                                                horizontalAlignment = Alignment.Start
                                            ){
                                                Text("$connectionCount")
                                                Text("Contacts")
                                            }
                                        }

                                    }


                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = userProfile.baseProfileData?.userName ?: "",
                                                fontWeight = FontWeight.Bold,
                                                style = AppTheme.typography.titleLarge,
                                                color = AppTheme.colorScheme.onTertiary
                                            )
                                        }
                                        IconButton(onClick = {screenValue = EditScreenChange.EDIT_NAME_SCREEN}) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = AppTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }


                        }

                        item { HorizontalDivider() }

                        item {

                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "Bio",
                                            fontWeight = FontWeight.Bold,
                                            style = AppTheme.typography.titleMedium,
                                            color = AppTheme.colorScheme.onTertiary
                                        )
                                    }

                                    IconButton(onClick = {screenValue = EditScreenChange.EDIT_ABOUT_SCREEN}) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = AppTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Text(
                                    text = userProfile.baseProfileData?.userBio ?: "",
                                    color = AppTheme.colorScheme.onTertiary,
                                    style = AppTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Normal
                                )
                            }

                        }

                        item { HorizontalDivider() }

                        //Social Account
                        item {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "Social Accounts",
                                        fontWeight = FontWeight.Bold,
                                        style = AppTheme.typography.titleMedium,
                                        color = AppTheme.colorScheme.onTertiary
                                    )
                                }

                                IconButton(onClick = {screenValue = EditScreenChange.EDIT_SOCIAL_ACCOUNTS}) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = AppTheme.colorScheme.primary
                                    )
                                }
                            }

                        }


                        item {

                            if (userProfile.baseProfileData?.social?.isNotEmpty() == true){

                                Row(
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Card(
                                        onClick = {},
                                        modifier = Modifier.size(42.dp)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Image(
                                                painter = painterResource(R.drawable.instagram),
                                                contentDescription = null
                                            )
                                        }
                                    }

                                }
                            }

                        }


                        item { HorizontalDivider() }

                        // Interest
                        item {

                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "Interests",
                                            fontWeight = FontWeight.Bold,
                                            style = AppTheme.typography.titleMedium,
                                            color = AppTheme.colorScheme.onTertiary
                                        )
                                    }

                                    IconButton(onClick = {screenValue = EditScreenChange.EDIT_INTERESTS}) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = AppTheme.colorScheme.primary
                                        )
                                    }
                                }

                                FlowRow (
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (userProfile.baseProfileData?.interests?.isNotEmpty() == true) {

                                        userProfile.baseProfileData.interests.forEach {
                                            AssistChip(
                                                onClick = {},
                                                label = {
                                                    Text(
                                                        it,
                                                        modifier = Modifier.padding(10.dp),
                                                        color = AppTheme.colorScheme.onTertiary
                                                    )
                                                },
                                                border = BorderStroke(
                                                    width = 1.dp,
                                                    color = Color.LightGray
                                                ),
                                            )
                                        }
                                    }

                                }
                            }

                        }

                    }

                    LoadingUI(isLoading = userProfile.isLoading)
                    
                    }

                }
            }

        EditScreenChange.EDIT_NAME_SCREEN->{
            EditField(
                onCancelClick = {screenValue = EditScreenChange.PROFILE_SCREEN},
                onSubmitClick = {
                    scope.launch {
                        userProfileViewModel.modifyName(userName = userName)
                            .collect{
                                when(it){
                                    is ResultState.Loading->{
                                        isLoading = true
                                    }
                                    is ResultState.Success->{
                                        userProfileViewModel.getUserProfile(userId = userUUID.toString())
                                        isLoading = false
                                        screenValue = EditScreenChange.PROFILE_SCREEN
                                    }
                                    is ResultState.Error->{
                                        isLoading = false
                                    }
                                }
                            }
                    }
                }
            ) {
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = {userName = it.toString() },
                    label = "Name",
                    placeHolder = "Enter your name",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        }

        EditScreenChange.EDIT_SOCIAL_ACCOUNTS->{

            EditField(
                onCancelClick = {screenValue = EditScreenChange.PROFILE_SCREEN},
                onSubmitClick = {

                    scope.launch {
                        userProfileViewModel.modifySocialAccount(
                            social = socialAccount
                        )
                            .collect{
                                when(it){
                                    is ResultState.Loading->{
                                        isLoading = true
                                    }
                                    is ResultState.Success->{
                                        userProfileViewModel.getUserProfile(userUUID.toString())
                                        isLoading = false
                                        screenValue = EditScreenChange.PROFILE_SCREEN
                                    }
                                    is ResultState.Error->{
                                        isLoading = false
                                    }
                                }
                            }
                    }

                }
            ) {
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = {socialAccount = it.toString()},
                    label = "LinkedIn",
                    placeHolder = "url",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        }

        EditScreenChange.EDIT_ABOUT_SCREEN->{

            EditField(
            onCancelClick = {screenValue = EditScreenChange.PROFILE_SCREEN},
            onSubmitClick = {
                scope.launch {
                    userProfileViewModel.modifyAbout(about = userAbout)
                        .collect{
                            when(it){
                                is ResultState.Loading->{
                                    isLoading = true
                                }
                                is ResultState.Success->{
                                    userProfileViewModel.getUserProfile(userUUID.toString())
                                    isLoading = false
                                    screenValue = EditScreenChange.PROFILE_SCREEN

                                }
                                is ResultState.Error->{
                                    isLoading = false
                                }
                            }
                        }
                }
            }
        ) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = {userAbout = it.toString()},
                label = "About",
                placeHolder = "What about you?",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
        }
    }

        EditScreenChange.EDIT_INTERESTS->{

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Interest", style = AppTheme.typography.headingLarge) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = AppTheme.colorScheme.background,
                            titleContentColor = AppTheme.colorScheme.onTertiary
                        ),
                        navigationIcon = {
                            IconButton(onClick = {screenValue = EditScreenChange.PROFILE_SCREEN}) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            if (interestSelectionViewModel.selectedInterests.isNotEmpty()){
                                IconButton(onClick = {

                                    scope.launch {
                                        userProfileViewModel.modifyInterests(
                                            interests = interestSelectionViewModel.selectedInterests
                                        )
                                            .collect{
                                                when(it){
                                                    is ResultState.Loading->{
                                                        isLoading = true
                                                    }
                                                    is ResultState.Success->{
                                                        userProfileViewModel.getUserProfile(userUUID.toString())
                                                        isLoading = false
                                                        screenValue = EditScreenChange.PROFILE_SCREEN
                                                    }
                                                    is ResultState.Error->{
                                                        isLoading = false
                                                    }
                                                }
                                            }
                                    }




                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            }

                        }

                    )
                },
                containerColor = AppTheme.colorScheme.background
            ) { paddingValues ->

                FlowRow(
                    modifier = Modifier.padding(paddingValues).padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    interestSelectionViewModel.allInterests.forEach {

                        AssistChip(
                            onClick = {
                                interestSelectionViewModel.toggleInterest(it)
                            },
                            label = {
                                Text(it, modifier = Modifier.padding(10.dp), color = AppTheme.colorScheme.onTertiary)
                            },
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (interestSelectionViewModel.selectedInterests.contains(it)) AppTheme.colorScheme.primary else Color.LightGray
                            ),
                        )

                    }


                }

            }


        }
        
        EditScreenChange.SETTING_SCREEN->{

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Setting", style = AppTheme.typography.headingLarge) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = AppTheme.colorScheme.background,
                            titleContentColor = AppTheme.colorScheme.onTertiary
                        ),
                        navigationIcon = {
                            IconButton(onClick = {screenValue = EditScreenChange.PROFILE_SCREEN}) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }

                    )
                },
                containerColor = AppTheme.colorScheme.background
            ) {paddingValues ->

                LazyColumn (
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    items(settingList){

                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {},
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(22.dp),
                                    painter = painterResource(it.icon),
                                    contentDescription = null,
                                    tint = AppTheme.colorScheme.primary

                                )
                                Text(
                                    text = it.item,
                                    fontWeight = FontWeight.Bold,
                                    color = AppTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                }

            }
        }
    }

    LoadingUI(isLoading = isLoading)


}


@Composable
fun EditField(
    onCancelClick:(Int)->Unit,
    onSubmitClick:()->Unit,
    content:@Composable ()->Unit
) {

    Column(modifier = Modifier) {

        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {onCancelClick.invoke(1)}) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
            IconButton(onClick = {onSubmitClick.invoke()}) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }


    }

}


val settingList = listOf(
    navItems(
        "About",
        R.drawable.info,
        R.drawable.home_bold,
        NestedScreens.MainScreen.Home.routes
    ),
    navItems(
        "Privacy Policy",
        R.drawable.user_lock,
        R.drawable.messages_bold,
        NestedScreens.MainScreen.Chat.UsersChat.routes
    ),
    navItems(
        "Term & Conditions",
        R.drawable.memo_circle_check,
        R.drawable.notification_bold,
        NestedScreens.MainScreen.Notification.routes
    ),
    navItems(
        "Feedback",
        R.drawable.feedback_hand,
        R.drawable.user_bold,
        NestedScreens.MainScreen.Account.Profile.routes
    ),
    navItems(
        "Log Out",
        R.drawable.sign_out_alt,
        R.drawable.user_bold,
        NestedScreens.MainScreen.Account.Profile.routes
    )
)
