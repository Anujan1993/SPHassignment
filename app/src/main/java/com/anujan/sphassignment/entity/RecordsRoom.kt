package com.anujan.sphassignment.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
        tableName = "RecordsRoom")
data class RecordsRoom (
        @PrimaryKey(autoGenerate = false)
        val _id:Int = 0,
        @ColumnInfo(name = "volume_of_mobile_data")
        val volume_of_mobile_data: String,
        val quarter : String,
        val _full_count : String,
        val rank : Double)