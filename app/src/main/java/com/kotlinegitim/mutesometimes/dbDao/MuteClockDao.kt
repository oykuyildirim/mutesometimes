package com.kotlinegitim.mutesometimes.dbDao

import androidx.room.*
import com.kotlinegitim.mutesometimes.models.MuteClock

@Dao

interface MuteClockDao {

    @Query("select * from ClockSet")
    fun allClocks() : List<MuteClock>

    @Insert
    fun Insert( clock: MuteClock) : Long

    @Query("SELECT * FROM ClockSet WHERE clock LIKE :clock")

    fun clockSame(clock: String): List<MuteClock>

    @Delete
    fun Delete( clock: MuteClock)

    @Update
    fun Update( clock: MuteClock)

}