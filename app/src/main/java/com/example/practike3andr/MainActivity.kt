package com.example.practike3andr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practike3andr.ui.navigation.BottomNavigationBar
import com.example.practike3andr.ui.navigation.Screen
import com.example.practike3andr.ui.screens.ActorDetailsScreen
import com.example.practike3andr.ui.screens.ActorsListScreen
import com.example.practike3andr.ui.screens.SearchScreen
import com.example.practike3andr.ui.screens.ProfileScreen
import com.example.practike3andr.ui.theme.Practike3ANDRTheme
import com.example.practike3andr.ui.viewmodel.ActorsViewModel
import com.example.practike3andr.ui.viewmodel.ActorsViewModelFactory
import com.example.practike3andr.data.remote.NetworkModule
import com.example.practike3andr.data.repository.ActorsRepositoryImpl
import com.example.practike3andr.domain.GetActorsUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            Practike3ANDRTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val actorsViewModel: ActorsViewModel = viewModel(factory = ActorsViewModelFactory(
        GetActorsUseCase(
            repository = ActorsRepositoryImpl(NetworkModule.actorsApi)
        )
    ))
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ActorsList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ActorsList.route) {
                ActorsListScreen(
                    viewModel = actorsViewModel,
                    onActorClick = { actor ->
                        navController.navigate("actor_details/${actor.id}")
                    }
                )
            }
            
            composable(Screen.Search.route) {
                SearchScreen()
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            
            composable("actor_details/{actorId}") { backStackEntry ->
                val actorId = backStackEntry.arguments?.getString("actorId")?.toIntOrNull()
                val actors by actorsViewModel.actors.collectAsState()
                val actor = actors.find { it.id == actorId }
                
                actor?.let {
                    ActorDetailsScreen(
                        actor = it,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } ?: run {
                    // Если актер не найден, возвращаемся назад
                    navController.popBackStack()
                }
            }
        }
    }
}