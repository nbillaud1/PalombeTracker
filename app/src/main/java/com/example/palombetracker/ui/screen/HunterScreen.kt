package com.example.palombetracker

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.palombetracker.ui.models.Hunter
import com.example.palombetracker.ui.models.HunterDao
import kotlinx.coroutines.launch // Import nécessaire pour les coroutines

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHunterScreen(navController: NavHostController, hunterDao: HunterDao) {
    // 1. Récupérer les données de la base en temps réel
    // Le collectAsState() transforme le Flow de Room en état Compose.
    val huntersList by hunterDao.getAllHunters().collectAsState(initial = emptyList<Hunter>())
    // 2. Créer un scope pour lancer l'insertion en arrière-plan
    val coroutineScope = rememberCoroutineScope()

    var hunterAdded by remember { mutableStateOf(false) }

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

            var newHunterName by remember { mutableStateOf("") }

            OutlinedTextField(
                value = newHunterName,
                onValueChange = { newHunterName = it; hunterAdded = false },
                label = { Text("Nom du nouveau chasseur") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (newHunterName.isNotBlank()) {
                        // 3. Insérer dans la base de données via une Coroutine
                        coroutineScope.launch {
                            hunterDao.insert(Hunter(name = newHunterName, killCount = 0))
                            hunterAdded = true
                            newHunterName = ""
                        }
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (hunterAdded) {
                Text("Chasseur ajouté avec succès !", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Liste des chasseurs déjà présents", style = MaterialTheme.typography.titleLarge)

            // 4. Afficher la vraie liste de la base de données
            huntersList.forEach { option ->
                Button(
                    onClick = {
                        coroutineScope.launch {
                            hunterDao.delete(option)
                        }
                    }
                ){
                    Text(" - ${option.name} avec ${option.killCount} palombes tuées", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}