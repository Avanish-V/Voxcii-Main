package com.byteapps.voxcii.Screens.Register

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.byteapps.voxcii.R
import com.byteapps.voxcii.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("ContextCastToActivity")
@Composable
fun SignInScreen(navHostController: NavHostController, onSignInClick: () -> Unit) {

   val activity = LocalContext.current as Activity

   LaunchedEffect(Unit) {
      WindowCompat.setDecorFitsSystemWindows(activity.window, false)
      activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
   }


   Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.fillMaxSize()
   ) {
      // Background with Glass Effect + Gradient
      Box(
         modifier = Modifier
            .fillMaxSize()
            .background(
               brush = Brush.verticalGradient(
                  colors = listOf(AppTheme.colorScheme.primary, AppTheme.colorScheme.tertiary), // Custom gradient
                  startY = 0.0f,
                  endY = Float.POSITIVE_INFINITY
               )
            )
            .blur(radius = 15.dp) // Apply blur for glass effect
            .graphicsLayer(alpha = 0.6f) // Lower opacity for frosted look
      )

      Column(
         modifier = Modifier.fillMaxSize(),
         verticalArrangement = Arrangement.Bottom
      ) {

         Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
         ) {


            Image(
               modifier = Modifier.size(200.dp),
               painter = painterResource(R.drawable.main_screen_logo),
               contentDescription = null,
               contentScale = ContentScale.Crop
            )

         }

         Column(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

            Text(
               "Welcome to\nVoxcii\nwhere, connect voices!",
               fontWeight = FontWeight.Bold,
               fontSize = 28.sp,
               color = Color.White, // Changed to White for contrast
               textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(24.dp))
            Text(
               "Make meaningful connections with like-minded people.",
               textAlign = TextAlign.Center,
               color = Color.White.copy(alpha = 0.8f),
               style = AppTheme.typography.titleMedium,
               fontWeight = FontWeight.Normal
            )

         }

         Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

            OutlinedButton(
               modifier = Modifier
                  .padding(horizontal = 40.dp, vertical = 100.dp)
                  .fillMaxWidth()
                  .height(52.dp),
               onClick = { onSignInClick.invoke() },
               border = BorderStroke(width = 1.5.dp, color = Color.LightGray),
               colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White) // White Text
            ) {
               Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
               ) {
                  Image(
                     modifier = Modifier.size(24.dp),
                     painter = painterResource(R.drawable.google),
                     contentDescription = "",
                  )
                  Text("Continue with Google", color = Color.White)
               }
            }

         }

      }
   }
}
