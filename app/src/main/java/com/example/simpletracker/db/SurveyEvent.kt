package com.example.simpletracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class SurveyEvent(
    @PrimaryKey val timestamp: Date,
    val rating: Int,
    val tags: List<String>
    )
