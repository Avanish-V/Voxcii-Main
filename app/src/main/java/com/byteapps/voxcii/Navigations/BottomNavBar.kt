package com.byteapps.nestednevigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.byteapps.karigar.presentation.Screens.bottomBarScreens.BottomBar.items
import com.byteapps.voxcii.ui.theme.AppTheme


@Composable
fun BottomAppBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    NavigationBar(

        containerColor = Color.White,
        modifier = Modifier.shadow(elevation = 5.dp),
        contentColor = Color.Transparent

    ) {

        items.forEachIndexed { index, item ->

            NavigationBarItem(
                icon = {

                    if (item.item == "Offers"){
                        BadgedBox(
                            badge = {
                                Box(Modifier.offset(x = 2.dp, y = (-2).dp).size(16.dp).background(color = Color.Red, shape = CircleShape), contentAlignment = Alignment.Center){
                                    Text("1", color = Color.White, style = MaterialTheme.typography.displaySmall)
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id =  if (destination?.route == item.route) item.iconBold else item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }else{
                        Icon(
                            painter = painterResource(id =  if (destination?.route == item.route) item.iconBold else item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                },
                label = { Text(item.item, fontWeight = if (destination?.route == item.route) FontWeight.Bold else FontWeight.Normal) },
                selected = destination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {

                    if (destination?.route != item.route) {
                        navController.navigate(item.route) {

                            popUpTo(navController.graph.findStartDestination().id) {

                                saveState = false
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }


                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    indicatorColor = Color.Transparent,
                    selectedTextColor = AppTheme.colorScheme.onTertiary
                )

            )
        }
    }
}