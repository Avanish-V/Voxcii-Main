package com.byteapps.voxcii.UIComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.byteapps.voxcii.R
import com.byteapps.voxcii.ui.theme.AppTheme

val featureList = listOf<FeatureItems>(
    FeatureItems(
        title = "Service Guarantee",
        description = "Book sessions at your convenience with our easy-to-use scheduling system.",
        icon = R.drawable.bullseye
    ),
    FeatureItems(
        title = "Flexible Scheduling",
        description = "Book sessions at your convenience with our easy-to-use scheduling system.",
        icon = R.drawable.calendar_clock
    ),
    FeatureItems(
        title = "Rich Resources",
        description = "Access exclusive resources, workshops, and learning materials from industry experts.",
        icon = R.drawable.book_open_cover
    ),

    )
data class FeatureItems(
    val title:String,
    val description:String,
    val icon:Int
)

@Composable
fun FeatureSingleCard(
    title: String,
    description: String,
    icon: Int,
) {

    Card (
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.onBackground
        )
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = AppTheme.colorScheme.primary
                )
                Text(
                    title,
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.onTertiary
                )
            }
            Text(
                text = description,
                color = AppTheme.colorScheme.onTertiary
            )
        }

    }


}
