package com.byteapps.voxcii.UIComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byteapps.voxcii.R
import com.byteapps.voxcii.ui.theme.AppTheme

@Composable
fun LoadingUI(isLoading:Boolean) {

    if (isLoading){

        Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center){
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = AppTheme.colorScheme.primary
            )
        }


    }

}

@Composable
fun StatusScreen(isActive:Boolean,text:String,image:Int) {

    if (isActive){

        Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(image),
                    contentDescription = null
                )
                Text(text = text, style = AppTheme.typography.titleMedium)
            }

        }


    }

}

@Composable
fun ConnectingLoadingUI(isLoading:Boolean) {

    if (isLoading){

        Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center){

            Column(
                modifier = Modifier.size(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = AppTheme.colorScheme.primary
                )
                Text("Finding match...")
            }

        }


    }

}

@Composable
fun MatchingLoadingAnimated(isLoading:Boolean,userImage:String) {

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.matchng))

            LottieAnimation(
                modifier = Modifier.size(250.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever,
                speed = 1f,
                restartOnPlay = true,
                contentScale = ContentScale.FillHeight
            )
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


        }
    }

}