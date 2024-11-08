package com.hackwithsodiq.gsearch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hackwithsodiq.gsearch.R
import com.hackwithsodiq.gsearch.navigation.Route
import com.hackwithsodiq.gsearch.ui.theme.DeepShadePurple
import com.hackwithsodiq.gsearch.ui.theme.DeepShape
import com.hackwithsodiq.gsearch.ui.theme.LightShade
import com.hackwithsodiq.gsearch.ui.theme.LightShadePurple

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(stringResource(R.string.home))
            },
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colorScheme.background,
        )
    }, backgroundColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Row {
                Box(modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .background(color = LightShade)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .clickable {
                        navController.navigate(Route.USERS.route)
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .background(color = Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_user_unselected),
                                tint = DeepShape,
                                contentDescription = "users"
                            )
                        }
                        Text(stringResource(R.string.users), fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))
                Box(modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .background(color = LightShadePurple)
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .clickable {
                        navController.navigate(Route.REPOSITORIES.route)
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .background(color = Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_repo),
                                tint = DeepShadePurple,
                                contentDescription = "repositories"
                            )
                        }
                        Text(stringResource(R.string.repositories), fontWeight = FontWeight.Medium)
                    }
                }
            }

        }
    }
}