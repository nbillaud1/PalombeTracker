package com.example.palombetracker

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var showValidation by remember { mutableStateOf(false) }

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
                onValueChange = { newHunterName = it; hunterAdded = false; showValidation = it.isNotBlank() },
                label = { Text("Nom du nouveau chasseur") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showValidation) {
                IconButton(
                    onClick = {
                        if (newHunterName.isNotBlank()) {
                            // 3. Insérer dans la base de données via une Coroutine
                            coroutineScope.launch {
                                hunterDao.insert(Hunter(name = newHunterName, killCount = 0))
                                hunterAdded = true
                                showValidation = false
                                newHunterName = ""
                            }
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Valider l'ajout", tint = Color(0xFF4CAF50))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (hunterAdded) {
                Text("Chasseur ajouté avec succès !", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Liste des chasseurs déjà présents", style = MaterialTheme.typography.titleLarge)

            // Afficher la vraie liste de la base de données
            huntersList.forEach { hunter ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        " - ${hunter.name} avec ${hunter.killCount} palombes tuées",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                hunterDao.delete(hunter)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer le chasseur",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}