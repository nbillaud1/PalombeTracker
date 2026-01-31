package com.example.palombetracker
// TODO : rajouter la date de tuerie / vol + le compteur par chasseur
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.palombetracker.ui.theme.PalombeTrackerTheme
import java.lang.System.console

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
        composable(route = "homeScreen") {
            HomeScreen(navController = navController)
        }
        composable(route = "woodPigeonScreen") {
            WoodPigeonScreen(navController = navController)
        }
        composable(route = "flightScreen") {
            FlightScreen(navController = navController)
        }
        composable(route = "newHunterScreen") {
            NewHunterScreen(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Palombe Tracker") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tableau de Bord",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Palombes Tuées: ${pigeonKills.size}", fontSize = 20.sp)
                    Text("Total Vols Observés: ${flights.size}", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("woodPigeonScreen") },
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text(text = "Gérer les palombes tuées", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("flightScreen") },
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text(text = "Gérer les vols", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("newHunterScreen") },
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text(text = "Enregistrer un chasseur", fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoodPigeonScreen(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedHunter by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Palombes Tuées") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Ajouter une palombe", style = MaterialTheme.typography.titleLarge)
            
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    value = selectedHunter,
                    onValueChange = {it -> selectedHunter = it},
                    readOnly = true,
                    label = { Text("Nom du chasseur") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    hunters.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedHunter = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (selectedHunter.isNotBlank()) {
                        pigeonKills.add(PigeonKill(selectedHunter))
                        selectedHunter = ""
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Historique (${pigeonKills.size})", style = MaterialTheme.typography.titleMedium)
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(pigeonKills.reversed()) { kill ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(
                            text = kill.hunter,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightScreen(navController: NavHostController) {
    var didLand by remember { mutableStateOf(false) }
    var selectedWindDirection by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestion des Vols") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text("Enregistrer un vol", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { didLand = !didLand }
                ) {
                    Text(
                        text = if (didLand) "Le vol s'est posé ! " else "Le vol ne s'est pas posé...",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = didLand,
                        onCheckedChange = { didLand = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                val options = listOf("Nord", "Sud", "Est", "Ouest", "Nord-Est", "Sud-Ouest", "Nord-Ouest", "Sud-Est", "Inconnue")
                var expanded by remember { mutableStateOf(false) }
                var selectedWindDirection by remember { mutableStateOf("") }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        value = selectedWindDirection,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Direction du vent") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedWindDirection = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = selectedWindDirection.isNotBlank(),
                    onClick = {
                        flights.add(Flight(didLand, selectedWindDirection))
                        didLand = false
                        selectedWindDirection = ""
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Ajouter le vol")
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Historique des vols (${flights.size})", style = MaterialTheme.typography.titleMedium)
            }

            items(flights.reversed()) { flight ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Vent: ${flight.windDirection}", fontWeight = FontWeight.Bold)
                        Text("Posé: ${if (flight.didLand) "Oui" else "Non"}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHunterScreen(navController: NavHostController) {
    var addedHunter by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chasseurs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Ajouter un nouveau chasseur", style = MaterialTheme.typography.titleLarge)

            var hunterToAdd by remember { mutableStateOf("") }
            OutlinedTextField(
                value = hunterToAdd,
                onValueChange = {it -> hunterToAdd = it; addedHunter = false},
                label = { Text("Nom du nouveau chasseur") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if(hunterToAdd.isNotBlank()) {
                        hunters.add(hunterToAdd)
                        hunterToAdd = ""
                        addedHunter = true
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(40.dp))

            if(addedHunter) {
                Text("Chasseur ajouté avec succès !", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}