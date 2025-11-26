package com.example.shoutcastplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoutcastplayer.ui.screens.FavoritesScreen
import com.example.shoutcastplayer.ui.screens.HomeScreen
import com.example.shoutcastplayer.ui.screens.PlayerScreen
import com.example.shoutcastplayer.ui.viewmodel.RadioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // We scope the ViewModel to the navigation graph so it persists across screens
    val viewModel: RadioViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(viewModel = viewModel, navController = navController)
        }
        composable(Screen.Player.route) {
            PlayerScreen(viewModel = viewModel, navController = navController)
        }
    }
}
