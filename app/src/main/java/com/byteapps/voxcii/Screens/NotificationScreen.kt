package com.byteapps.voxcii.Screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.byteapps.voxcii.Network.JoinedOptions.data.RequestNotificationDTO
import com.byteapps.voxcii.Network.JoinedOptions.presentation.JoinedOptionsViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.UIComponents.LoadingUI
import com.byteapps.voxcii.UIComponents.StatusScreen
import com.byteapps.voxcii.Utils.ResultState
import com.byteapps.voxcii.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {

    val scope = rememberCoroutineScope()
    val joinedOptionsViewModel = koinInject<JoinedOptionsViewModel>()
    val requestNotification = joinedOptionsViewModel.connectionRequest.collectAsState().value

    LaunchedEffect(Unit) {
        joinedOptionsViewModel.getRequests()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications",
                    style = AppTheme.typography.headingLarge)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background
                )
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { padding ->


        StatusScreen(
            isActive = requestNotification.isEmpty(),
            text = "No notification!",
            image = R.drawable.notifiacation
        )

        LazyColumn(modifier =Modifier.padding(padding)) {
            items(requestNotification) { notification ->
                NotificationItemView(
                    notification = notification,
                    onAcceptClick = {
                        OutlinedIconButton(onClick = {
                            scope.launch {
                                joinedOptionsViewModel.acceptConnectionRequest(notification.senderId)
                                    .collect{
                                        when(it){
                                            is ResultState.Success -> {
                                                joinedOptionsViewModel.getRequests()
                                            }
                                            is ResultState.Error -> {}
                                            is ResultState.Loading -> {}
                                        }
                                    }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                        }

                    },
                    onRejectClick = {
                        scope.launch {
                            joinedOptionsViewModel.rejectConnectionRequest(notification.senderId)
                        }
                        joinedOptionsViewModel.getRequests()
                    }
                )
            }
        }


    }
}

@Composable
fun NotificationItemView(
    notification: RequestNotificationDTO,
    onAcceptClick: @Composable ()->Unit,
    onRejectClick:()->Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        AsyncImage(
            model = notification.userImage,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
            ,
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = notification.userName, style = AppTheme.typography.titleMedium)
                    Text("2 days ago", style = MaterialTheme.typography.bodyMedium)
                }
                Row (horizontalArrangement = Arrangement.spacedBy(12.dp)){

                    OutlinedIconButton(onClick = {onRejectClick.invoke()}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                    onAcceptClick.invoke()
                }
            }



            //Text(text = notification.message, style = MaterialTheme.typography.bodyMedium)
        }

    }
}
