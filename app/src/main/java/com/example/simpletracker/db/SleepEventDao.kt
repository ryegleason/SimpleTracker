package com.example.simpletracker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SleepEventDao {

    @Insert
    fun insert(sleepEvent: SleepEvent)

    @Query("SELECT * FROM SleepEvent ORDER BY timestamp desc LIMIT 1")
    fun getMostRecentSleepEvent() : List<SleepEvent>
}