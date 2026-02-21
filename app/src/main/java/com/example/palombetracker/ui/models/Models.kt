package com.example.palombetracker.ui.models

import java.time.LocalDate
import java.time.LocalDateTime
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Flight(
    val didLand: Boolean,
    val windDirection: String,
    val date: LocalDate,
    val hour: Long,
    val pFromCage: Boolean,
    val pFromTower: Boolean,
    val flightWorked: Boolean,
)

@Entity(tableName = "hunters")
data class Hunter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Un ID unique pour la BDD
    val name: String,
    var killCount: Int = 0,
)

data class PigeonKill(
    val isolated: Boolean, // if not isolated, ask for the flight informations
    val flight: Flight?,
    val hunter: Hunter?,
    val hour: LocalDateTime?,
    val date: LocalDate,
)

data class Year(
    val year: Int,
    val flights: List<Flight>,
    val hunters: List<Hunter>,
    var pigeonKill: List<PigeonKill>,
)

