package com.kotlinegitim.mutesometimes.dbDao

import androidx.room.*
import com.kotlinegitim.mutesometimes.models.Location

@Dao
interface  LocationDao {


    @Query("select * from LocationSet")
    fun allLocation() : List<Location>

    @Insert
    fun InsertLoc( loc: Location) : Long

    @Query("SELECT * FROM LocationSet WHERE location LIKE :location")

    fun LocSame(location: String): List<Location>

    @Delete
    fun DeleteLoc( loc: Location)

    @Update
    fun UpdateLoc( loc: Location)

}