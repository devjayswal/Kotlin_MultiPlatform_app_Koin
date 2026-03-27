package com.example.test1.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.test1.data.model.User
import com.example.test1.ui.auth.AuthViewModel
import com.example.test1.ui.components.ConnectivityStatus
import com.example.test1.ui.navigation.Screen
import com.example.test1.ui.navigation.bottomNavItems
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(authViewModel: AuthViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            Column {
                ConnectivityStatus()
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        tonalElevation = 8.dp,
                        shadowElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                    ) {
                        NavigationBar(
                            containerColor = Color.Transparent,
                            tonalElevation = 0.dp,
                            modifier = Modifier.height(72.dp)
                        ) {
                            bottomNavItems.forEach { item ->
                                val selected = currentRoute == item.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    label = { 
                                        Text(
                                            item.title,
                                            style = MaterialTheme.typography.labelSmall
                                        ) 
                                    },
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount != null) {
                                                    Badge(
                                                        containerColor = MaterialTheme.colorScheme.error,
                                                        contentColor = Color.White
                                                    ) { 
                                                        Text(item.badgeCount.toString()) 
                                                    }
                                                } else if (item.hasBadge) {
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                item.icon, 
                                                contentDescription = item.title,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                View1(koinViewModel(), 1, innerPadding)
            }
            composable(Screen.Settings.route) {
                View2(koinViewModel(), innerPadding)
            }
            composable(Screen.Chat.route) {
                View3(koinViewModel(), innerPadding)
            }
            composable(Screen.Profile.route) {
                View4(koinViewModel(), navController, innerPadding)
            }
            composable(Screen.View5.route) { backStackEntry ->
                val userJson = backStackEntry.arguments?.getString("userJson")
                val user = userJson?.let { Json.decodeFromString<User>(it) } ?: User(name = "Unknown")
                View5(koinViewModel(), user, authViewModel)
            }
        }
    }
}
