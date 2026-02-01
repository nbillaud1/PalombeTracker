package com.example.palombetracker

import android.adservices.ondevicepersonalization.IsolatedWorker
import java.time.LocalDate

data class Flight(
    val didLand: Boolean,
    val windDirection: String,
    val date: LocalDate,
    val heure: Long,
    val pFromCage: Boolean,
    val pFromTower: Boolean,
    val flightWorked: Boolean,
)

data class Hunter(
    val name: String,
    var killCount: Int = 0,
)

data class PigeonKill(
    val isolated: Boolean, // if not isolated, ask for the flight informations
    val flight: Flight,
    val hunter: Hunter,
    val heure: Long,
    val date: LocalDate,
)

data class Year(
    val year: Int,
    val flights: List<Flight>,
    val hunters: List<Hunter>,
    var pigeonKill: List<PigeonKill>,
)

