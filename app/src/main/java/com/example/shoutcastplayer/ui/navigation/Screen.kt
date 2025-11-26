package com.example.shoutcastplayer.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorites : Screen("favorites")
    object Player : Screen("player")
}
