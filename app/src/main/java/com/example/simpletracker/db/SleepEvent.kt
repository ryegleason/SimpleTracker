package com.example.simpletracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class SleepEvent(
    @PrimaryKey val timestamp: Date,
    val isWakeup: Boolean
)
