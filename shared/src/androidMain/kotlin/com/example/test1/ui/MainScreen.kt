package com.example.test1.ui

import android.R.attr.name
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.test1.model.User
import com.example.test1.ui.viewModels.ViewModel1
import com.example.test1.ui.viewModels.ViewModel2
import com.example.test1.ui.viewModels.ViewModel3
import com.example.test1.ui.viewModels.ViewModel4
import com.example.test1.ui.viewModels.ViewModel5
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

@Composable
fun MainScreen() {



    val items = listOf(
        BottomNavItem(
            title = "Home",
            route = "view1",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Filled.Home,
            hasNews = false,
        ),
        BottomNavItem(
            title = "Settings",
            route = "view2",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Filled.Settings,
            hasNews = false,
        ),
        BottomNavItem(
            title = "Chat",
            route = "view3",
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Filled.Notifications,
            hasNews = true,
            badgeCount = 45
        ),
        BottomNavItem(
            title = "Profile",
            route = "view4",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Filled.Person,
            hasNews = true,
        ),
//        BottomNavItem(
//            title = "V5",
//            route = "view5",
//            selectedIcon = Icons.Filled.Info,
//            unselectedIcon = Icons.Filled.Info,
//            hasNews = true,
//        )
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isFullScreen = currentRoute?.startsWith("view5") == true
    val user = User(name="Dev Jayswal",age = 21,city = "Ahmedabad",address = "Ahmedabad",phone = "+919589883539",
        email ="Devjayswal404@gmail.com",isStudent = true,grades = listOf(10,20,30,40,50),
        relatives = listOf(User(name="Shiv Jayswal",age = 23)))

    fun sendToProfile() {
        val userJson = Json.encodeToString(user)
        val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
        navController.navigate("view5/$encodedUserJson")
    }

    if (isFullScreen) {

        // 🔥 FULL SCREEN (NO bottom bar)
        NavHost(
            navController = navController,
            startDestination = "view1"
        ) {
            composable("view1") {
                val viewModel = koinViewModel<ViewModel1>()
                View1(viewModel, name)
            }
            composable("view2") {
                val viewModel = koinViewModel<ViewModel2>()
                View2(viewModel)
            }
            composable("view3") {
                val viewModel = koinViewModel<ViewModel3>()
                View3(viewModel)
            }
            composable("view4") {
                val viewModel = koinViewModel<ViewModel4>()
                View4(viewModel, navController)
            }
            composable("view5/{userJson}") { backStackEntry ->
                val userJsonEncoded = backStackEntry.arguments?.getString("userJson")
                val userFromArg = userJsonEncoded?.let {
                    Json.decodeFromString<User>(it)
                } ?: user

                val viewModel = koinViewModel<ViewModel5>()
                View5(viewModel, userFromArg)
            }
        }

    } else {

        // 🔥 MAIN APP (WITH bottom bar)
        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(item.title) },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge { Text(item.badgeCount.toString()) }
                                        } else if (item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex)
                                            item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = "view1",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("view1") {
                    val viewModel = koinViewModel<ViewModel1>()
                    View1(viewModel, name)
                }
                composable("view2") {
                    val viewModel = koinViewModel<ViewModel2>()
                    View2(viewModel)
                }
                composable("view3") {
                    val viewModel = koinViewModel<ViewModel3>()
                    View3(viewModel)
                }
                composable("view4") {
                    val viewModel = koinViewModel<ViewModel4>()
                    View4(viewModel, navController)
                }
                composable("view5/{userJson}") { backStackEntry ->
                    val userJsonEncoded = backStackEntry.arguments?.getString("userJson")
                    val userFromArg = userJsonEncoded?.let {
                        Json.decodeFromString<User>(it)
                    } ?: user

                    val viewModel = koinViewModel<ViewModel5>()
                    View5(viewModel, userFromArg)
                }
            }
        }
    }
}
