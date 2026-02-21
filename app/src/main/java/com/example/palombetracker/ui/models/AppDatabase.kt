package com.example.palombetracker.ui.models

import androidx.room.Database
import androidx.room.RoomDatabase

// Si tu as plusieurs entités (Flight, PigeonKill), tu les rajoutes ici séparées par des virgules
@Database(entities = [Hunter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hunterDao(): HunterDao
}