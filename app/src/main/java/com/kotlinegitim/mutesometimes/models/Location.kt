package com.kotlinegitim.mutesometimes.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "LocationSet", indices = [Index(value = ["location"], unique = true)])
data class Location(@PrimaryKey val id:Int?,
                    val location : String,
                    val title: String,
                    val active : Boolean,
)