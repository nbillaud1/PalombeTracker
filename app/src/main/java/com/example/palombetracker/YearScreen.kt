package com.example.palombetracker

import android.service.autofill.OnClickAction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearScreen(navController: NavHostController, year: Int) {
    var expanded by remember { mutableStateOf(false) }
    var selectedYear = LocalDate.now().year.toString() // TODO : faire que ce soit un objet de type Year aussi
    val option = loadYears()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bienvenue sur Palombes Tracker !!! ") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Veuillez choisir l'année à consulter",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    value = selectedYear,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Année") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    option.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(year) },
                            onClick = {
                                selectedYear = year
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("homeScreen/$selectedYear") },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ){
                Text("Voir l'année $selectedYear", fontSize = 18.sp)
            }
        }
    }
}

fun loadYears(): List<String> {
    //TODO vrai fonctionnement
    return listOf("2023", "2024", "2025", "2026", "2027");
}