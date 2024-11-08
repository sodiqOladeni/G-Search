package com.hackwithsodiq.gsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hackwithsodiq.gsearch.navigation.GSearchBottomBar
import com.hackwithsodiq.gsearch.navigation.Route
import com.hackwithsodiq.gsearch.ui.screens.HomeScreen
import com.hackwithsodiq.gsearch.ui.screens.RepositoriesScreen
import com.hackwithsodiq.gsearch.ui.screens.UserDetailsScreen
import com.hackwithsodiq.gsearch.ui.screens.UsersScreen
import com.hackwithsodiq.gsearch.ui.theme.GSearchTheme
import com.hackwithsodiq.gsearch.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GSearchTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        GSearchBottomBar(navController, currentDestination)
                    }) { innerPadding ->
                    val viewModel: UserViewModel = hiltViewModel()
                    val usersUiState by viewModel.userDetailsScreenUiState.collectAsState()

                    NavHost(
                        navController = navController,
                        startDestination = Route.HOME.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Route.HOME.route) {
                            HomeScreen(navController)
                        }
                        composable(Route.REPOSITORIES.route) {
                            RepositoriesScreen(navController)
                        }
                        composable(Route.USERS.route) {
                            UsersScreen(navController){
                                viewModel.setSelectedUser(it)
                                navController.navigate("users/detail")
                            }
                        }
                        composable("users/detail") {
                            UserDetailsScreen(navController, usersUiState.user!!, viewModel)
                        }
                    }
                }
            }
        }
    }
}