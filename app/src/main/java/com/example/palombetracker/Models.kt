package com.example.palombetracker

data class PigeonKill(
    val hunter: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class Flight(
    val didLand: Boolean,
    val windDirection: String,
    val timestamp: Long = System.currentTimeMillis()
)
