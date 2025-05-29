package com.example.practicaanimacion.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    suspend fun getTopScores(): List<ScoreEntity>

    @Query("SELECT COUNT(*) FROM scores WHERE playerName = :name")
    suspend fun existsByName(name: String): Int
}
