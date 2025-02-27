package com.byteapps.voxcii.Screens.SearchFlow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.byteapps.voxcii.Network.Chats.data.UserChatsDTO
import com.byteapps.voxcii.R
import com.byteapps.voxcii.Screens.LikeToggleButton
import com.byteapps.voxcii.Screens.NameImage
import com.byteapps.voxcii.Screens.ToggleButton
import com.byteapps.voxcii.Screens.convertTimestampToTime
import com.byteapps.voxcii.Utils.NestedScreens
import com.byteapps.voxcii.ui.theme.AppTheme
import com.byteapps.voxcii.ui.theme.Background
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class,)
@Composable
fun SearchScreen(navHostController: NavHostController) {

    var searchValue by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.background(color = Background)) {

        SearchBar(

            inputField = {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = AppTheme.colorScheme.primary
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 10.dp),
                        value = searchValue,
                        onValueChange = { searchValue = it },
                        placeholder = {
                            Text(
                                text = "Search",
                                color = AppTheme.colorScheme.onTertiary
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                        ),
                        colors = TextFieldDefaults.colors(

                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Black
                        ),
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(R.drawable.search),
                                contentDescription = null
                            )
                        },
                        shape = RoundedCornerShape(32.dp)
                    )
                }

            },
            expanded = true,
            onExpandedChange = {

            },
            shape = RoundedCornerShape(10.dp),
            colors = SearchBarDefaults.colors(
                containerColor = AppTheme.colorScheme.background,
                dividerColor = Color.Transparent
            )

        ) {

//            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                items(10){
//
//                }
//            }


        }

    }
}

@Composable
fun MentorSingleCard(chatItem: UserChatsDTO, onClick: () -> Unit) {


    Card(
        shape = RoundedCornerShape(0.dp),
        onClick={onClick.invoke()},
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                model = chatItem.userImage,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    chatItem.userName,
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.onTertiary

                )
                Text(
                    chatItem.lastMessage.lastMessage,
                    color = AppTheme.colorScheme.onTertiary
                )
            }
            Text(convertTimestampToTime(chatItem.lastMessage.timeStamp))

        }
    }
}



