package com.byteapps.savvy.LocationPermission.Permission

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.byteapps.voxcii.R
import com.byteapps.wiseschool.GeoFencing.Permission.PermissionViewModel

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentDeclined:Boolean,
    onDismiss:()->Unit,
    onOkClick:()->Unit,
    onGoToAppsSettingsClick:()->Unit,
    modifier:Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {

            Column {
                HorizontalDivider()
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter){
                    TextButton(
                        onClick = {
                            if (isPermanentDeclined) {
                                onGoToAppsSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                    ) {
                        Text(text = if(isPermanentDeclined) {
                            "Grant permission"

                        } else {
                            "OK"
                        },)
                    }
                }
            }
        },
        title = {
            Text(text = "Permission required")
        },
        text = {
           Text(
               text = permissionTextProvider.getDescription(isPermanentDeclined = isPermanentDeclined),
               textAlign = TextAlign.Center
           )
        },
        icon = {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.location),
                contentDescription = null
            )
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(8.dp)

    )

}

interface PermissionTextProvider{
    fun getDescription(isPermanentDeclined: Boolean):String

}

class locationPermissionText: PermissionTextProvider {

    override fun getDescription(isPermanentDeclined: Boolean): String {
       return if (isPermanentDeclined){
           "It seems you permanently declined location permission."+
                   "You can go to the app settings to grant it."
       }else{
           "This app needs to access your location."
       }
    }

}


@Composable
fun StateDialog(dialogQueue : SnapshotStateList<String>,permissionViewModel:PermissionViewModel,context: Context) {
    dialogQueue.forEach {permission->

        PermissionDialog(
            permissionTextProvider = when (permission) {
                Manifest.permission.RECORD_AUDIO -> {
                    locationPermissionText()
                }

                else -> return@forEach
            },
            isPermanentDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                permission
            ),
            onDismiss = {
                permissionViewModel.dismissDialog()
            },
            onOkClick = {
                permissionViewModel.dismissDialog()
            },
            onGoToAppsSettingsClick = {

                permissionViewModel.openAppSettings(context)

            }
        )
    }
}

