package com.example.simpletracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@Database(entities = [SleepEvent::class, SurveyEvent::class], version = 1)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {

    abstract fun sleepEventDao() : SleepEventDao
    abstract fun surveyEventDao() : SurveyEventDao
}

const val DB_NAME = "events.db"
private var singletonInstance: EventDatabase? = null

fun getDatabaseSingleton(context: Context?): EventDatabase? {
    if (singletonInstance == null) {
        singletonInstance = databaseBuilder(
            context!!,
            EventDatabase::class.java, DB_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    return singletonInstance
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun joinTags(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun separateTags(value: String?): List<String>? {
        return value?.split(",")
    }
}
