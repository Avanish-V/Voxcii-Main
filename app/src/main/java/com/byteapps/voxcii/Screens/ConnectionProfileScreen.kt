package com.byteapps.voxcii.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.byteapps.voxcii.Network.JoinedOptions.presentation.JoinedOptionsViewModel
import com.byteapps.voxcii.Network.UserProfile.presentation.UserProfileViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.UIComponents.CustomTextField
import com.byteapps.voxcii.UIComponents.EditScreenChange
import com.byteapps.voxcii.UIComponents.LoadingUI
import com.byteapps.voxcii.Utils.NestedScreens
import com.byteapps.voxcii.Utils.ResultState
import com.byteapps.voxcii.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ConnectionProfileScreen(navHostController: NavHostController) {

    val userProfileViewModel = koinInject<UserProfileViewModel>()
    val joinedOptionsViewModel = koinInject<JoinedOptionsViewModel>()

    LaunchedEffect(Unit) {
        joinedOptionsViewModel.getConnectionCount()
    }
    val userProfile = userProfileViewModel.userBaseProfile.collectAsStateWithLifecycle().value

    val userUUID by remember {
        mutableStateOf(navHostController.currentBackStackEntry?.savedStateHandle?.get<String>("USER_ID"))
    }

    LaunchedEffect(Unit) {
        userProfileViewModel.getUserProfile(userUUID.toString())
    }

    val interestList = remember { mutableStateListOf("Entrepreneurship", "Coding","Reading") }

    var isLoading by remember { mutableStateOf(false) }


    userProfile.baseProfileData.let {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {  },
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
                    )

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

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {

                            Card(
                                Modifier.size(150.dp),
                                shape = CircleShape,
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = AppTheme.colorScheme.primary
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = AppTheme.colorScheme.background
                                )
                            ) {
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .clip(CircleShape)) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = userProfile.baseProfileData?.userImage,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Text(
                                text = userProfile.baseProfileData?.userName ?: "",
                                fontWeight = FontWeight.Bold,
                                style = AppTheme.typography.headingLarge,
                                color = AppTheme.colorScheme.onTertiary
                            )



                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ){
                                Image(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(R.drawable.heart_outline),

                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.Red)
                                )
                                Text(text = userProfile.baseProfileData?.likes.toString(), style = AppTheme.typography.titleMedium)
                            }


                        }


                    }

                    item {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                navHostController.navigate(NestedScreens.MainScreen.Account.SendMessage.routes).apply {
                                    navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_ID",userUUID)
                                    navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_NAME",userProfile.baseProfileData?.userName)
                                    navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_IMAGE",userProfile.baseProfileData?.userImage)
                                }
                            }
                        ) {
                            Text(text = "Message")
                        }
                    }

                    item {

                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "About",
                                        fontWeight = FontWeight.Bold,
                                        style = AppTheme.typography.titleMedium,
                                        color = AppTheme.colorScheme.onTertiary
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

                    // Bio
                    item {

                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "Intrests",
                                        fontWeight = FontWeight.Bold,
                                        style = AppTheme.typography.titleMedium,
                                        color = AppTheme.colorScheme.onTertiary
                                    )

                                }

                            }

                            FlowRow (
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                interestList.forEach {
                                    AssistChip(
                                        onClick = {},
                                        label = {
                                            Text(it, modifier = Modifier.padding(10.dp), color = AppTheme.colorScheme.onTertiary)
                                        },
                                        border = BorderStroke(width = 1.dp, color = Color.LightGray),
                                    )
                                }
                            }
                        }
                    }

                    item {

                        Text(
                            text = "Social",
                            fontWeight = FontWeight.Bold,
                            style = AppTheme.typography.titleMedium,
                            color = AppTheme.colorScheme.onTertiary
                        )

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

                }
            }
        }
    }

    LoadingUI(isLoading = isLoading)


}
