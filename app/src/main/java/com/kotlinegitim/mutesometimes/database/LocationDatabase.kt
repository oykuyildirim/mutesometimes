package com.kotlinegitim.mutesometimes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlinegitim.mutesometimes.dbDao.LocationDao
import com.kotlinegitim.mutesometimes.models.Location

@Database(entities = [Location::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {


    abstract fun LocationDao() : LocationDao


}