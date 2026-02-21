package com.example.palombetracker

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
import com.example.palombetracker.ui.screen.FlightScreen
import com.example.palombetracker.ui.models.Flight
import com.example.palombetracker.ui.models.Hunter
import com.example.palombetracker.ui.models.PigeonKill
import com.example.palombetracker.ui.models.Year
import com.example.palombetracker.ui.screen.HomeScreen
import com.example.palombetracker.ui.screen.RecapScreen
import com.example.palombetracker.ui.screen.WoodPigeonScreen
import com.example.palombetracker.ui.screen.YearScreen
import com.example.palombetracker.ui.theme.PalombeTrackerTheme
import java.time.LocalDate
import androidx.room.Room
import com.example.palombetracker.ui.models.AppDatabase
import com.example.palombetracker.ui.models.HunterDao

// Simple state storage (resets on app restart as per this simple implementation)
val pigeonKills = mutableStateListOf<PigeonKill>()
val flights = mutableStateListOf<Flight>()
val registeredHunters = mutableStateListOf<Hunter>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "palombes-tracker-db"
        )
            .allowMainThreadQueries() // Ajoute ça UNIQUEMENT pour tes tests du début
            .build()

        val hunterDao = db.hunterDao()

        // Exemple pour lire les chasseurs :
        val hunters = hunterDao.getAllHunters()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PalombeTrackerTheme {
                AppNavigation(db.hunterDao())
            }
        }
    }
}

@Composable
fun AppNavigation(hunterDao: HunterDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "yearScreen"
    ) {
        composable(route = "woodPigeonScreen") {
            WoodPigeonScreen(navController = navController)
        }
        composable(route = "flightScreen") {
            FlightScreen(navController = navController)
        }
        composable(route = "newHunterScreen") {
            NewHunterScreen(navController = navController, hunterDao = hunterDao)
        }
        composable(route = "yearScreen") {
            YearScreen(navController = navController, year = LocalDate.now().year)
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