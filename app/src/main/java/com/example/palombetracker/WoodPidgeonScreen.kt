package com.example.palombetracker
// TODO : pouvoir sélectionner un vol si la palombe n'est pas isolée ou pouvoir crér un vol si la palombe n'est pas isolée
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoodPigeonScreen(navController: NavHostController) {
    var isolated by remember { mutableStateOf(true) }
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
                            text = { Text(option.name) },
                            onClick = {
                                selectedHunter = option.name
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isolated = !isolated }
            ) {
                Text(
                    text = "Palombe isolée ?",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isolated,
                    onCheckedChange = { isolated = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            //TODO
            Button(
                onClick = {
                    if (selectedHunter.isNotBlank()) {
                        pigeonKills.add(PigeonKill(
                            isolated, null, hunters.find { it.name == selectedHunter }, LocalDateTime.now(), LocalDate.now()
                        ))
                        val hunterToAddKill = hunters.find { it.name == selectedHunter }
                        if (hunterToAddKill != null){
                            hunterToAddKill.killCount++
                        }
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
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Le ${kill.date.dayOfMonth}/${kill.date.monthValue}/${kill.date.year}", fontWeight = FontWeight.Bold) // à ${flight.hour}
                            Text(text = (kill.hunter?.name ?: "Inconnu") + " a tué une" + if(kill.isolated) " palombe isolée" else " palombe non isolée :")
                        }
                    }
                }
            }
        }
    }
}