package com.example.palombetracker.ui.screen
//TODO : savoir cmb de pigeons ont été laché / nb palombes posées
// TODO : savoir si le vol a été travaillé : mettre une coche Oui [] NON []. à la place du switch
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
import com.example.palombetracker.ui.models.Flight
import com.example.palombetracker.flights
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightScreen(navController: NavHostController) {
    var didLand by remember { mutableStateOf(false) }
    var flightWorked by remember { mutableStateOf(false) }
    var pFromCage by remember { mutableStateOf(false) }
    var pFromTower by remember { mutableStateOf(false) }

    var selectedWindDirection by remember { mutableStateOf("") }
    var flightHour by remember { mutableStateOf("") }

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
                Text("Enregistrer un vol :", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(32.dp))

                Text("Sélectionnez l'heure du vol : ")

                /*val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val times = remember {
                    generateSequence(LocalTime.MIN) { it.plusMinutes(30) }
                        .takeWhile { it.isBefore(LocalTime.MAX) }
                        .map { it.format(formatter) }
                        .toList()
                }

                var expandedTime by remember { mutableStateOf(false) }
                var flightHour by remember { mutableStateOf("") }

                ExposedDropdownMenuBox(
                    expanded = expandedTime,
                    onExpandedChange = { expandedTime = !expandedTime }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        value = flightHour,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Heure du vol") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expandedTime,
                        onDismissRequest = { expandedTime = false }
                    ) {
                        times.forEach { time ->
                            DropdownMenuItem(
                                text = { Text(time) },
                                onClick = {
                                    flightHour = time
                                    expandedTime = false
                                }
                            )
                        }
                    }
                }*/

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { didLand = !didLand }
                ) {
                    Text(
                        text = "Le vol s'est posé ! ",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = didLand,
                        onCheckedChange = { didLand = it }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { didLand = !didLand }
                ) {
                    Text(
                        text = "Le vol ne s'est pas posé...",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = !didLand,
                        onCheckedChange = { didLand = it }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                val windDirection = listOf("Nord", "Sud", "Est", "Ouest", "Nord-Est", "Sud-Ouest", "Nord-Ouest", "Sud-Est", "Inconnue")
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
                        windDirection.forEach { option ->
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { flightWorked = !flightWorked }
                ) {
                    Text(
                        text = "Le vol a été travaillé ? ",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = flightWorked,
                        onCheckedChange = { flightWorked = it }
                    )
                }

                if(flightWorked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { pFromTower = !pFromTower }
                    ) {
                        Text(
                            text = "pigeons lachés depuis la palombière ?",
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = pFromTower,
                            onCheckedChange = { pFromTower = it }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { pFromCage = !pFromCage }
                    ) {
                        Text(
                            text = "pigeons lachés depuis autre part (préciser à Nathan d'où) ? ",
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = pFromCage,
                            onCheckedChange = { pFromCage = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                //TODO
                Button(
                    enabled = selectedWindDirection.isNotBlank(), //&& flightHour.isNotBlank()
                    onClick = {
                        flights.add(
                            Flight(
                                didLand,
                                selectedWindDirection,
                                LocalDate.now(),
                                System.currentTimeMillis(),
                                pFromCage,
                                pFromTower,
                                flightWorked
                            )
                        )
                        didLand = false
                        pFromCage = false
                        pFromTower = false
                        flightWorked = false
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
                        Text("Le ${flight.date.dayOfMonth}/${flight.date.monthValue}/${flight.date.year}", fontWeight = FontWeight.Bold) // à ${flight.hour}
                        Text("Vent: ${flight.windDirection}")
                        Text("Posé: ${if (flight.didLand) "Oui" else "Non"}")
                        Text("Vol travaillé ? ${if (flight.flightWorked) "Oui" else "Non"}")
                        if(flight.flightWorked){
                            if (flight.pFromTower) {
                                Text("  - pigeons lachés: \n    depuis la Palombière")
                            }
                            if(flight.pFromCage){
                                Text("  - pigeons lachés: \n    depuis autre part (dire à Nathan d'où)")
                            }
                        }
                    }
                }
            }
        }
    }
}