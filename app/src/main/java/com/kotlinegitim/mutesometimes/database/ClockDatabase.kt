package com.kotlinegitim.mutesometimes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlinegitim.mutesometimes.dbDao.MuteClockDao
import com.kotlinegitim.mutesometimes.models.MuteClock

@Database(entities = [MuteClock::class], version = 1, exportSchema = false)
abstract class ClockDatabase : RoomDatabase() {


    abstract fun MuteClockDao() : MuteClockDao


}