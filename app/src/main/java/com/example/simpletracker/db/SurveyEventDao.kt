package com.example.simpletracker.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface SurveyEventDao {

    @Insert
    fun insert(surveyEvent: SurveyEvent)
}