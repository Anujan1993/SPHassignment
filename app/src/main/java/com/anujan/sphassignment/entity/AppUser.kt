package com.anujan.sphassignment.entity

import androidx.room.*

@Entity(
    tableName = "user",
    indices = [Index(value = ["email"], unique = true)])
data class AppUser(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int = 0,
    val username: String,

    @ColumnInfo(name = "user_phone")
    val userPhone: String?,

    val country: String?,

    val password: String,

    var email: String)
