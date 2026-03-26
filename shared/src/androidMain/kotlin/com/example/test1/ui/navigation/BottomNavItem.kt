package com.example.test1.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val hasBadge: Boolean = false,
    val badgeCount: Int? = null
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
    BottomNavItem("Users", Screen.Settings.route, Icons.Default.Settings),
    BottomNavItem("News", Screen.Chat.route, Icons.Default.Email, hasBadge = true, badgeCount = 12),
    BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person)
)
