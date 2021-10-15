package com.anujan.sphassignment.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "MainRecords")
data class MainRecords (
    @PrimaryKey(autoGenerate = false)
    val _id:Int = 0,
    @ColumnInfo(name = "volume_of_mobile_data")
    val volume_of_mobile_data: String?,
    val quarter: String?)
