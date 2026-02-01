package com.example.palombetracker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHunterScreen(navController: NavHostController) {
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
                onValueChange = {it -> newHunterName = it; hunterAdded = false},
                label = { Text("Nom du nouveau chasseur") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if(newHunterName.isNotBlank()) {
                        hunters.add(Hunter(newHunterName, 0))
                        newHunterName = ""
                        hunterAdded = true
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(40.dp))

            if(hunterAdded) {
                Text("Chasseur ajouté avec succès !", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Liste des chasseurs déjà présents", style = MaterialTheme.typography.titleLarge)
            hunters.forEach { option ->
                Text(" - ${option.name} avec ${option.killCount} palombes tuées", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}