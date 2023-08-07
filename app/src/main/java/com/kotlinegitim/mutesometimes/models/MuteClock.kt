package com.kotlinegitim.mutesometimes.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "ClockSet", indices = [Index(value = ["clock"], unique = true)])
data class MuteClock (@PrimaryKey val id:Int?,
                      val clock:String,
                      val title: String,
                      val active : Boolean)