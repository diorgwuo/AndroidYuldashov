package com.example.practike3andr.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object ActorsList : Screen("actors_list", "Актеры", Icons.Default.Home)
    object Search : Screen("search", "Поиск", Icons.Default.Search)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(
        Screen.ActorsList,
        Screen.Search,
        Screen.Profile
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Очищаем стек навигации до корневого элемента
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Избегаем множественных копий одного экрана
                        launchSingleTop = true
                        // Восстанавливаем состояние при повторном выборе
                        restoreState = true
                    }
                }
            )
        }
    }
}
