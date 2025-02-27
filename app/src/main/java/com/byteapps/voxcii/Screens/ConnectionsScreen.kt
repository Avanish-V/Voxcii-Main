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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.byteapps.voxcii.Network.JoinedOptions.data.RequestNotificationDTO
import com.byteapps.voxcii.Network.JoinedOptions.presentation.ConnectionsResultState
import com.byteapps.voxcii.Network.JoinedOptions.presentation.JoinedOptionsViewModel
import com.byteapps.voxcii.R
import com.byteapps.voxcii.Screens.SearchFlow.MentorSingleCard
import com.byteapps.voxcii.UIComponents.LoadingUI
import com.byteapps.voxcii.Utils.NestedScreens
import com.byteapps.voxcii.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsScreen(navHostController: NavHostController) {

    val scope = rememberCoroutineScope()
    val joinedOptionsViewModel = koinInject<JoinedOptionsViewModel>()
    val connections = joinedOptionsViewModel.connections.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        joinedOptionsViewModel.getConnections()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Connections",
                        style = AppTheme.typography.headingLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigation"
                        )
                    }
                }
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { padding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            LoadingUI(isLoading = connections.isLoading)

            if (connections.connectionList.isNotEmpty()) {

                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    items(connections.connectionList) { connections ->
                        ConnectionsItemView(
                            onItemClick = {
                                navHostController.navigate(NestedScreens.MainScreen.Account.ConnectionProfile.routes).apply {
                                    navHostController.currentBackStackEntry?.savedStateHandle?.set("USER_ID",connections.senderId)
                                }
                            },
                            connectionData = connections,
                            onRejectClick = {
                                scope.launch {
                                    joinedOptionsViewModel.rejectConnectionRequest(connections.senderId)
                                }
                                joinedOptionsViewModel.getRequests()
                            }
                        )
                    }
                }

            } else {


            }
        }

    }
}


@Composable
fun ConnectionsItemView(
    onItemClick: () -> Unit,
    connectionData: RequestNotificationDTO,
    onRejectClick: () -> Unit
) {
    Card(
        onClick = {
            onItemClick.invoke()
        },
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.background
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = connectionData.userImage,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = connectionData.userName, style = AppTheme.typography.titleMedium)
                    Text(
                        text = connectionData.userBio,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedIconButton(onClick = { onRejectClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

