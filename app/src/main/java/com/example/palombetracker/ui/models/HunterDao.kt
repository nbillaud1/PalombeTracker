package com.example.palombetracker.ui.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HunterDao {
    // Récupérer tous les chasseurs
    @Query("SELECT * FROM hunters")
    fun getAllHunters(): Flow<List<Hunter>>

    // Ajouter un chasseur
    @Insert
    suspend fun insert(hunter: Hunter): Long

    @Delete
    suspend fun delete(hunter: Hunter): Int
}