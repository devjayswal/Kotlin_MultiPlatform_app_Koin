package com.example.test1.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    object View5 : Screen("view5/{userJson}") {
        fun createRoute(userJson: String) = "view5/$userJson"
    }
}
