package com.example.palombetracker
// TODO : le compteur par chasseur
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.palombetracker.ui.theme.PalombeTrackerTheme
import java.time.LocalDate

// Simple state storage (resets on app restart as per this simple implementation)
val pigeonKills = mutableStateListOf<PigeonKill>()
val flights = mutableStateListOf<Flight>()
val hunters = mutableStateListOf("Paul", "Gilles", "Régis")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalombeTrackerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "homeScreen"
    ) {
        composable(route = "woodPigeonScreen") {
            WoodPigeonScreen(navController = navController)
        }
        composable(route = "flightScreen") {
            FlightScreen(navController = navController)
        }
        composable(route = "newHunterScreen") {
            NewHunterScreen(navController = navController)
        }
        composable(route = "homeScreen") {
            HomeScreen(navController = navController, year = LocalDate.now().year)
        }
        composable(
            route = "homeScreen/{year}",
            arguments = listOf(
                navArgument("year") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            HomeScreen(navController = navController, year = year)
        }
        composable(
            route = "recap/{year}",
            arguments = listOf(
                navArgument("year") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 0
            RecapScreen(navController = navController, year = year)
        }
    }
}