package com.hackwithsodiq.gsearch.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun GSearchBottomBar(
    navController: NavController,
    currentDestination: NavDestination?
){
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = Color.Red,
        elevation = 20.dp,
    ) {
        Route.entries.toTypedArray().forEach { route ->
            val selected = currentDestination?.route == route.route
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(if (selected) route.selectedIcon else route.unSelectedIcon),
                        contentDescription = route.name
                    )
                },
                label = { Text(stringResource(route.title)) },
                selected = selected,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Black,
            )
        }
    }
}